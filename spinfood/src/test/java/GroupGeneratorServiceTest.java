import controller.Registration;
import models.enums.Courses;
import models.enums.FoodPreferences;
import models.data.GroupModel;
import models.data.LocationModel;
import models.data.PairModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.service.GroupGeneratorService;
import models.service.PairGeneratorService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the GroupGeneratorService class.
 * It tests the functionality of generating groups of pairs and writing the group data to a CSV file.
 *
 * Each test method in this class is annotated with the @Test annotation.
 * The setUp method, annotated with @BeforeEach, is run before each test to initialize the test data.
 *
 * @see GroupGeneratorService
 */
class GroupGeneratorServiceTest {

     List<PairModel> pairs;
     LocationModel partyLocation;
     GroupGeneratorService groupGeneratorService;

    /**
     * This method is run before each test to initialize the test data.
     */
    @BeforeEach
    void setUp() {
        pairs = new ArrayList<>();

        var reg = new Registration();
        try {
            reg.registerParticipants("src/Data/teilnehmerliste.csv");
            reg.registerPartyLocation("src/Data/partylocation.csv");

        } catch (Exception e) {
            // Print the exception details for debugging
        }

        PairGeneratorService pairGeneratorService = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        pairs = pairGeneratorService.generatePairs();

        partyLocation = new LocationModel(reg.getPartyLocation().getLongitude(),reg.getPartyLocation().getLatitude());
    }

    /**
     * This test checks if the generateGroups method of the GroupGeneratorService class correctly generates groups.
     */
    @Test
    void testGenerateGroups() {
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        var groups = groupGeneratorService.generateGroups();
        assertNotNull(groups, "Generated groups should not be null");
        assertFalse(groups.isEmpty());
        System.out.println("testGenerateGroups completed successfully");

    }

    /**
     * This test checks if the generateGroups method of the GroupGeneratorService class correctly creates and distributes groups among the courses.
     */
    @Test
    void testGenerateGroupsCreatesCorrectGroups() {
        // Arrange
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);

        // Act
        List<GroupModel> groups = groupGeneratorService.generateGroups();

        // Assert
        assertNotNull(groups, "Generated groups should not be null");
        assertFalse(groups.isEmpty(), "Generated groups should not be empty");

        // Check that the groups are correctly distributed among the courses
        long appetizerGroupsCount = groups.stream().filter(g -> g.getCourse() == Courses.APPETIZER).count();
        long mainCourseGroupsCount = groups.stream().filter(g -> g.getCourse() == Courses.MAIN).count();
        long dessertGroupsCount = groups.stream().filter(g -> g.getCourse() == Courses.DESSERT).count();

        assertEquals(appetizerGroupsCount, mainCourseGroupsCount, "The number of appetizer groups should equal the number of main course groups");
        assertEquals(mainCourseGroupsCount, dessertGroupsCount, "The number of main course groups should equal the number of dessert groups");

        // Check that each group has 3 pairs
        for (GroupModel group : groups) {
            assertEquals(3, group.getPairs().size(), "Each group should contain 3 pairs");
        }
        System.out.println("testGenerateGroupsCreatesCorrectGroups completed successfully");
    }

    /**
     * Checks the uniqueness of each pair meetings throughout the courses
     * */

    @Test
    void testUniquniessOfPairs(){
       groupGeneratorService =  new GroupGeneratorService(pairs, partyLocation);
       groupGeneratorService.generateGroups();
       var res = pairs.stream()
               .filter(p -> !p.isSuccessorPair())
               .allMatch(this::checkDiningPairs);
       assertTrue(res);
    }

    private boolean checkDiningPairs(PairModel pairModel){
        var map = pairModel.getDiningPairs();

        var pairsInAppetizerCourse =  map.get(Courses.APPETIZER);
        Set<PairModel> uniquePairs = new HashSet<>(pairsInAppetizerCourse);

       var pairsInMainCourse =  map.get(Courses.MAIN);
       uniquePairs.addAll(pairsInMainCourse);

       var pairsInDessertCourse =  map.get(Courses.DESSERT);
       uniquePairs.addAll(pairsInDessertCourse);

       return uniquePairs.size() == 6;
    }

    /**
     * This test checks if the toCSV method of the GroupGeneratorService class correctly writes the group data to a CSV file.
     * @throws IOException if an I/O error occurs
     */
    @Test
    void testToCSV() throws IOException {
        // Arrange
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        groupGeneratorService.generateGroups();
        String outputFilePath = "test_output.csv";

        // Act
        groupGeneratorService.toCSV(outputFilePath);

        // Assert
        File file = new File(outputFilePath);
        assertTrue(file.exists(), "Output file should be created");
        assertTrue(file.length() > 0, "Output file should not be empty");

        List<String> lines = Files.readAllLines(file.toPath());
        assertFalse(lines.isEmpty(), "Output file should contain data");

        // Clean up
        Files.deleteIfExists(file.toPath());
    }

    /**
     * This test checks if the toCSV method of the GroupGeneratorService class throws an exception when an invalid file path is provided.
     */
    @Test
    void testToCSVThrowsException() {
        // Arrange
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        groupGeneratorService.generateGroups();
        String invalidOutputFilePath = "\0"; // Null character in the file path should trigger an IOException

        // Act and Assert
        assertThrows(RuntimeException.class, () -> groupGeneratorService.toCSV(invalidOutputFilePath));
    }

    /**
     * This test checks if the toCSV method of the GroupGeneratorService class correctly writes the group data to the CSV file.
     * @throws IOException
     */
    @Test
    void testToCSVWritesDataCorrectly() throws IOException {
        // Arrange
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        groupGeneratorService.generateGroups();
        String outputFilePath = "test_output.csv";

        // Act
        groupGeneratorService.toCSV(outputFilePath);

        // Assert
        File file = new File(outputFilePath);
        assertTrue(file.exists(), "Output file should be created");
        assertTrue(file.length() > 0, "Output file should not be empty");

        List<String> lines = Files.readAllLines(file.toPath());
        assertFalse(lines.isEmpty(), "Output file should contain data");

        // Clean up
        Files.deleteIfExists(file.toPath());
    }



    /**
     * This test checks if the toCSV method of the GroupGeneratorService class correctly writes the group data to the CSV file.
     * @throws IOException
     */
    @Test
    void testToCSVWritesGroupDataCorrectly() throws IOException {
        // Arrange
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        groupGeneratorService.generateGroups();
        String outputFilePath = "test_output.csv";

        // Act
        groupGeneratorService.toCSV(outputFilePath);

        // Assert
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFilePath))) {
            reader.readLine(); // Skip the column names
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                assertEquals(12, data.length, "Each line should contain 12 data fields");
            }
        }

        // Clean up
        Files.deleteIfExists(Paths.get(outputFilePath));
    }

    /**
     * checks if meat groups contain veggies or vegans
     * */
    @Test
    void testInvalidFoodPreferences(){
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        var groups = groupGeneratorService.generateGroups();

        var res =groups.stream()
                .filter(g -> g.getGroupFoodPreference() == FoodPreferences.MEAT)
                .allMatch(this::hasNotVeganOrVeggiePair);

        assertTrue(res);
    }
    private boolean hasNotVeganOrVeggiePair(GroupModel group){
        var pairs = group.getPairs();
        var preferences = new ArrayList<FoodPreferences>();

        for(var pair: pairs)
            preferences.add(pair.getMainFoodPreference());
        return !(preferences.contains(FoodPreferences.VEGAN ) || preferences.contains(FoodPreferences.VEGGIE));
    }
    /**
     * checks if vegan or vegan groups contain meat consumers
     * */
    @Test
    void testInvalidFoodPreferences2(){
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        var groups = groupGeneratorService.generateGroups();

        var res =groups.stream()
                .filter(g -> g.getGroupFoodPreference() == FoodPreferences.VEGGIE
                        || g.getGroupFoodPreference() == FoodPreferences.VEGAN)
                .allMatch(this::hasNotMeatPair);

        assertTrue(res);
    }
    private boolean hasNotMeatPair(GroupModel group){
        var pairs = group.getPairs();
        var preferences = new ArrayList<FoodPreferences>();

        for(var pair: pairs)
            preferences.add(pair.getMainFoodPreference());
        return !(preferences.contains(FoodPreferences.MEAT ));
    }

    /**
     * checks if group of any food eaters have meat as preference
     * */
    @Test
    void testGroupPreferenceOfAnyFoodEaters(){
        groupGeneratorService = new GroupGeneratorService(pairs, partyLocation);
        var groups = groupGeneratorService.generateGroups();
        var res = groups.stream()
                .filter(groupModel -> groupModel.getPairs().get(0).getMainFoodPreference() == FoodPreferences.NONE
                && groupModel.getPairs().get(1).getMainFoodPreference() == FoodPreferences.NONE
                && groupModel.getPairs().get(2).getMainFoodPreference() == FoodPreferences.NONE)
                .allMatch(groupModel -> groupModel.getGroupFoodPreference() == FoodPreferences.MEAT);
        assertTrue(res);
    }
}
