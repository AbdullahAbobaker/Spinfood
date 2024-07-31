package models.service;

import com.opencsv.CSVWriter;
import models.enums.Courses;
import models.enums.FoodPreferences;
import models.data.GroupModel;
import models.data.LocationModel;
import models.data.PairModel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating groups of participants for the Spinfood event.
 * It implements the GroupsGenerator interface.
 * The class uses a list of PairModel objects and a LocationModel object to generate groups.
 * The groups are generated based on food preferences, age differences, and kitchen availability.
 * The class also provides methods to generate groups from the remaining participants.
 */
public class GroupGeneratorService implements GroupsGenerator {

    private List<PairModel> pairs;
    private List<GroupModel> groups;

    private List<GroupModel> appetizerGroups;
    private int appetizerGroupCounter = 1;

    private List<GroupModel> mainCourseGroups;
    private int mainCourseGroupCounter = 1;

    private List<GroupModel> dessertGroups;
    private int dessertGroupCounter = 1;

    private final int CLUSTER_SIZE = 9;
    private LocationModel partyLocation;

    private List<PairModel> successorPairs;

    /**
     * Constructs a new GroupGeneratorService with the given list of pairs and party location.
     *
     * @param pairs the list of pairs
     * @param partyLocation the location of the party
     */
    public GroupGeneratorService(List<PairModel> pairs, LocationModel partyLocation) {
        this.pairs = pairs;
        this.groups = new ArrayList<>();
        this.appetizerGroups = new ArrayList<>();
        this.mainCourseGroups = new ArrayList<>();
        this.dessertGroups = new ArrayList<>();
        this.partyLocation = partyLocation;
        successorPairs = new ArrayList<>();
    }

    /**
     * Generates groups of participants for the Spinfood event.
     *
     * @return a list of GroupModel objects representing the groups
     */
    @Override
    public List<GroupModel> generateGroups() {
        createGroups();
        groups.addAll(appetizerGroups);
        groups.addAll(mainCourseGroups);
        groups.addAll(dessertGroups);
        return groups;
    }

    /**
     * Gets the successor pairs for the generated groups.
     * The method should return a list of PairModel objects, each representing a pair of participants that are successors.
     *
     * @return a list of successor pairs
     */
    @Override
    public List<PairModel> getSuccessorPairs() {
        return successorPairs;
    }

    /**
     * Creates the groups of participants for the Spinfood event.
     * The method first separates the pairs into three lists based on their food preferences: vegans and vegetarians, meat eaters, and any food eaters.
     * The method then balances the vegan and vegetarian list and the meat eater list by adding pairs from the any food eater list until the size of these lists is a multiple of the cluster size.
     * The any food eater list is then balanced by removing pairs until its size is a multiple of the cluster size.
     * The removed pairs are added to the successorParticipants list.
     * The method then creates clusters from each list and creates groupings for each cluster.
     * Each cluster is divided into four unique arrangements, and each arrangement is assigned a course.
     * The pairs in the last arrangement are also assigned a course based on their distance to the party location.
     */
    private void createGroups() {
        var vegansAndVeggies =  pairs.stream()
                .filter(p -> p.getMainFoodPreference() == FoodPreferences.VEGAN || p.getMainFoodPreference() == FoodPreferences.VEGGIE)
                .sorted(Comparator.comparing(PairModel::getAgeDifference))
                .collect(Collectors.toCollection(ArrayList::new));
        var meatEaters =  pairs.stream()
                .filter(p -> p.getMainFoodPreference() == FoodPreferences.MEAT)
                .sorted(Comparator.comparing(PairModel::getAgeDifference))
                .collect(Collectors.toCollection(ArrayList::new));
        var anyFoodEaters =  pairs.stream()
                .filter(p -> p.getMainFoodPreference() == FoodPreferences.NONE)
                .sorted(Comparator.comparing(PairModel::getAgeDifference))
                .collect(Collectors.toCollection(ArrayList::new));

        balanceList(vegansAndVeggies, anyFoodEaters);
        balanceList(meatEaters, anyFoodEaters);
        balanceAnyFoodEatersList(anyFoodEaters);

        var veganAndVeggiesClusters = createClusters(vegansAndVeggies);
        var meatEaterClusters = createClusters(meatEaters);
        var anyFoodEaterClusters = createClusters(anyFoodEaters);

        createGroupingsForCluster(veganAndVeggiesClusters);
        createGroupingsForCluster(meatEaterClusters);
        createGroupingsForCluster(anyFoodEaterClusters);

    }

    /**
     * Balances the primary list by adding elements from the fallback list until the size of the primary list is a multiple of CLUSTER_SIZE.
     * Elements are removed from the fallback list and added to the primary list.
     *
     * @param primary the primary list of PairModel objects
     * @param fallback the fallback list of PairModel objects
     */
    private void balanceList(List<PairModel> primary, List<PairModel> fallback){
        Random random = new Random();
        while(primary.size() % CLUSTER_SIZE !=0)
            primary.add(fallback.remove(random.nextInt(fallback.size())));
    }

    /**
     * Balances the anyFoodEaters list by removing elements until the size of the list is a multiple of CLUSTER_SIZE.
     * Elements are removed from the anyFoodEaters list and added to the successorParticipants list.
     *
     * @param anyFoodEaters the list of PairModel objects with any food preference
     */
    private void balanceAnyFoodEatersList(List<PairModel> anyFoodEaters){
        Random random = new Random();
        while(anyFoodEaters.size() % CLUSTER_SIZE != 0){
            var pairAsASuccessor = anyFoodEaters.remove(random.nextInt(anyFoodEaters.size()));
            successorPairs.add(pairAsASuccessor);
            pairAsASuccessor.setSuccessorPair(true);
        }
    }

    /**
     * Creates clusters from the given list of PairModel objects.
     * Each cluster is a sublist of the given list, with a size of CLUSTER_SIZE or less.
     *
     * @param initialList the initial list of PairModel objects
     * @return a list of clusters
     */
    private List<List<PairModel>> createClusters(List<PairModel> initialList){
        List<List<PairModel>> clusters = new ArrayList<>();
        int listSize = initialList.size();

        for(int i = 0; i< listSize; i+=CLUSTER_SIZE){
            int endIndex = Math.min(listSize, i + CLUSTER_SIZE);
            List<PairModel> cluster = initialList.subList(i, endIndex);
            clusters.add(cluster);
        }
        return clusters;
    }

    /**
     * Creates groupings for each cluster of PairModel objects.
     * Each cluster is divided into four unique arrangements, and each arrangement is assigned a course.
     *
     * @param clusters the list of clusters
     */
    private void createGroupingsForCluster(List<List<PairModel>> clusters) {
        for (List<PairModel> cluster : clusters) {
            var arrangements = UniquePairArrangements.generateUniqueArrangements(cluster, 4);
            for(int i = 0; i < Courses.values().length; i++){
                var listOfGroups =  arrangements.get(i);
                for (GroupModel group : listOfGroups) {
                    group.setGroupFoodPreference(determineGroupFoodPreference(group));
                    group.setCourse(Courses.values()[i]);
                    addGroupToCourse(Courses.values()[i], group);
                    updateDininingPairs(group, Courses.values()[i]);
                }
            }
            CourseAssignmentService.assignCourses(cluster, partyLocation,  arrangements.get(3));
        }
    }

    /**
     * Adds a group to the appropriate course list based on the given course.
     * The group is also assigned a group number based on the course.
     *
     * @param course the course
     * @param optimalGroup the group to be added
     */
    private void addGroupToCourse(Courses course, GroupModel optimalGroup) {
        switch (course) {
            case APPETIZER:
                appetizerGroups.add(optimalGroup);
                optimalGroup.setAppetizerGroupNumber(appetizerGroupCounter++);
                optimalGroup.setCourse(Courses.APPETIZER);
                break;
            case MAIN:
                mainCourseGroups.add(optimalGroup);
                optimalGroup.setMainCourseGroupNumber(mainCourseGroupCounter++);
                optimalGroup.setCourse(Courses.MAIN);
                break;
            case DESSERT:
                dessertGroups.add(optimalGroup);
                optimalGroup.setDessertGroupNumber(dessertGroupCounter++);
                optimalGroup.setCourse(Courses.DESSERT);
                break;
        }
    }
    private void updateDininingPairs(GroupModel group, Courses course){
       var pairs =  group.getPairs();
       var first = pairs.get(0);
       var second = pairs.get(1);
       var third = pairs.get(2);

       var history = first.getDiningPairs().getOrDefault(course, new HashSet<>());
       history.addAll(List.of(second, third));
       first.getDiningPairs().put(course, history);

       var history1 = second.getDiningPairs().getOrDefault(course, new HashSet<>());
       history1.addAll(List.of(first, third));
       second.getDiningPairs().put(course, history1);

       var history2 = third.getDiningPairs().getOrDefault(course, new HashSet<>());
       history2.addAll(List.of(first, second));
       third.getDiningPairs().put(course, history2);
    }

    /**
     * Determines the food preference of a group based on the food preferences of the pairs in the group.
     *
     * @param groupModel the group
     * @return the food preference of the group
     */
    private FoodPreferences determineGroupFoodPreference(GroupModel groupModel) {
        var pairs = groupModel.getPairs();
        var preferences = pairs.stream()
                .collect(Collectors.groupingBy(PairModel::getMainFoodPreference, Collectors.counting()));

        if ((preferences.containsKey(FoodPreferences.VEGGIE) || preferences.containsKey(FoodPreferences.VEGAN))
                && preferences.containsKey(FoodPreferences.MEAT)) {
            return null;
        }
        if (preferences.getOrDefault(FoodPreferences.VEGAN, 0L) >= 2) {
            return FoodPreferences.VEGAN;
        }
        if (preferences.getOrDefault(FoodPreferences.VEGGIE, 0L) >= 2) {
            return FoodPreferences.VEGGIE;
        }
        if (preferences.containsKey(FoodPreferences.VEGGIE) && preferences.containsKey(FoodPreferences.VEGAN)) {
            return FoodPreferences.VEGAN;
        }
        return FoodPreferences.MEAT;
    }

    /**
     * Exports the groups of participants to a CSV file.
     * The CSV file includes columns for participant names, joint registration, kitchen location, food preference, pair number, course group numbers, kitchen supplier, and cooking course.
     * The groups are sorted by food preference before being written to the CSV file.
     *
     * @param outputFilePath the path of the output CSV file
     * @throws RuntimeException if an I/O error occurs
     */
    public void toCSV(String outputFilePath) {
        List<String[]> lines = new ArrayList<>();
        groups.sort(Comparator.comparing(GroupModel::getGroupFoodPreference));
        for (GroupModel group : groups) {
            var pairs = group.getPairs();
            for (PairModel pair : pairs) {
                lines.add(pair.getData());
            }
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath, StandardCharsets.UTF_8), ';',
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {

            for (String[] line : lines) {
                writer.writeNext(line, false);
                System.out.println(Arrays.toString(line));
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
