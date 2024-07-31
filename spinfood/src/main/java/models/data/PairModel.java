package models.data;

import models.enums.Courses;
import models.enums.FoodPreferences;
import models.enums.Gender;
import models.service.LocationCalculatorService;

import java.util.*;

/**
 * The PairModel class represents a pair of participants in the Spinfood event.
 * Each pair consists of two ParticipantModel objects.
 * The class also includes properties for joint registration, kitchen location, main food preference, pair number, course group numbers, kitchen supplier, cooking status, assigned course, and distance to the party location.
 * The class provides methods to get and set these properties.
 * The class also provides a method to calculate the age difference between the participants in the pair, and a method to get the data of the pair in a string array format.
 */
public class PairModel {
    private ParticipantModel participant1;
    private ParticipantModel participant2;
    private boolean jointRegistration;
    private double kitchenLongitude = -1;
    private double kitchenLatitude = -1;
    private FoodPreferences  mainFoodPreference;
    private int pairNumber;
    private int mainCourseGroupNumber = -1;
    private int appetizerGroupNumber = -1;
    private int dessertGroupNumber = -1;
    private boolean kitchenSupplier;
    private boolean isCooking;
    private Courses cookingCourse;
    private double distanceToPartyLocation;
    private Map<Courses, Set<PairModel>> diningPairs;
    private double pathLength;
    private Map<Courses, Double> distancesForCourses;
    public PairModel(ParticipantModel participant1, ParticipantModel participant2) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        diningPairs = new HashMap<>();
    }
    private boolean isSuccessorPair;

    public double getDistanceToPartyLocation() {
        return distanceToPartyLocation;
    }

    public void setDistanceToPartyLocation(double distanceToPartyLocation) {
        this.distanceToPartyLocation = distanceToPartyLocation;
    }

    public boolean isSuccessorPair() {
        return isSuccessorPair;
    }

    public void setSuccessorPair(boolean successorPair) {
        isSuccessorPair = successorPair;
    }

    public int getDessertGroupNumber() {
        return dessertGroupNumber;
    }

    public void setCooking(boolean cooking) {
        isCooking = cooking;
    }

    public void setCookingCourse(Courses course) {
        this.cookingCourse = course;
    }

    public void setMainFoodPreference(FoodPreferences mainFoodPreference) {
        this.mainFoodPreference = mainFoodPreference;
    }

    public void setKitchenSupplier(boolean kitchenSupplier) {
        this.kitchenSupplier = kitchenSupplier;
    }

    public boolean getKitchenSupplier(){
        return kitchenSupplier;
    }
    public ParticipantModel getParticipant1() {
        return participant1;
    }

    public ParticipantModel getParticipant2() {
        return participant2;
    }

    public FoodPreferences getMainFoodPreference() {
        return mainFoodPreference;
    }


    public void setParticipant1(ParticipantModel participant1) {
        this.participant1 = participant1;
    }

    public void setParticipant2(ParticipantModel participant2) {
        this.participant2 = participant2;
    }

    public boolean isJointRegistration() {
        return jointRegistration;
    }

    public void setJointRegistration(boolean jointRegistration) {
        this.jointRegistration = jointRegistration;
    }

    public double getKitchenLongitude() {
        return kitchenLongitude;
    }

    public void setKitchenLongitude(double kitchenLongitude) {
        this.kitchenLongitude = kitchenLongitude;
    }

    public double getKitchenLatitude() {
        return kitchenLatitude;
    }

    public void setKitchenLatitude(double kitchenLatitude) {
        this.kitchenLatitude = kitchenLatitude;
    }

    public int getPairNumber() {
        return pairNumber;
    }

    public void setPairNumber(int pairNumber) {
        this.pairNumber = pairNumber;
    }

    public Map<Courses, Set<PairModel>> getDiningPairs() {
        return diningPairs;
    }

    public int getMainCourseGroupNumber() {
        return mainCourseGroupNumber;
    }

    public void setMainCourseGroupNumber(int mainCourseGroupNumber) {
        this.mainCourseGroupNumber = mainCourseGroupNumber;
    }

    public int getAppetizerGroupNumber() {
        return appetizerGroupNumber;
    }


    public boolean isKitchenSupplier() {
        return kitchenSupplier;
    }

    public int  getAgeDifference() {
        return Math.abs(participant1.getAgeRange() - participant2.getAgeRange());
    }

    public boolean isCooking() {
        return isCooking;
    }

    public Courses getCookingCourse() {
        return cookingCourse;
    }

    public void setAppetizerGroupNumber(int appetizerGroupCounter) {
        appetizerGroupNumber = appetizerGroupCounter;
    }

    public void setDessertGroupNumber(int dessertGroupCounter) {
        dessertGroupNumber = dessertGroupCounter;
    }
    /**
     *  Returns the number of women in the pair.
     * */
    public int getNumberOfWomen() {
        int numberOfWomen = 0;
        if (participant1.getSex() == Gender.FEMALE) {
            numberOfWomen++;
        }
        if (participant2.getSex() == Gender.FEMALE) {
            numberOfWomen++;
        }
        return numberOfWomen;
    }
    /**
     *  Returns the number of others in the pair.
     * */
    public int getNumberOfOthers(){
        int numberOfOthers = 0;
        if (participant1.getSex() != Gender.FEMALE) {
            numberOfOthers++;
        }
        if (participant2.getSex() != Gender.FEMALE) {
            numberOfOthers++;
        }
        return numberOfOthers;
    }
    /**
     *  gives the preference deviation of the pair.
     * */
    public int getPreferenceDeviation() {
        int preference1 = getPreferenceValue(participant1.getFoodPreferences());
        int preference2 = getPreferenceValue(participant2.getFoodPreferences());

        return  Math.abs(preference1  - preference2);

    }
    /**
     * translates the preference to a value.
     * */
    private int getPreferenceValue(FoodPreferences foodPreferences) {
        int preferenceValue = 0;
        if (foodPreferences == FoodPreferences.VEGGIE) {
            preferenceValue = 1;
        }
        if (foodPreferences == FoodPreferences.VEGAN) {
            preferenceValue = 2;
        }
        return preferenceValue;
    }
    /**
     *  calculates the path length of the pair.
     * */
    private void calculatePathLength() {
        List<PairModel> nextCookingPairs = new ArrayList<>();
        distancesForCourses = new HashMap<>();
        for(Courses course : Courses.values()){
            if(course != cookingCourse) {
                Set<PairModel> meetingPairsForCourse = diningPairs.get(course);
                var nextCookingPair = meetingPairsForCourse.stream()
                        .filter(pair -> pair.getCookingCourse() == course)
                        .toList();
                if (nextCookingPair.size() == 1) {
                    nextCookingPairs.add(nextCookingPair.get(0));
                }
            }
        }

        if(cookingCourse == Courses.APPETIZER){
            calculateForAppetizerCookingPair(nextCookingPairs);
        }
        if(cookingCourse == Courses.MAIN){
            calculateForMainCookingPair(nextCookingPairs);
        }
        if(cookingCourse == Courses.DESSERT){
            calculateForDessertCookingPair(nextCookingPairs);
        }
    }

    /**
     *  calculates the distance for the dessert cooking pair.
     * */
    private void calculateForDessertCookingPair(List<PairModel> nextCookingPairs) {
        var appetizerCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.APPETIZER)
                .findFirst()
                .get();
        var mainCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.MAIN)
                .findFirst()
                .get();
        var calculator = new LocationCalculatorService(
                new LocationModel(appetizerCookingPair.getKitchenLongitude(), appetizerCookingPair.getKitchenLatitude()),
                new LocationModel(this.getKitchenLongitude(), this.getKitchenLatitude()));
        distancesForCourses.put(Courses.APPETIZER, calculator.calculateDistance());

        var calculator2 = new LocationCalculatorService(
                new LocationModel(appetizerCookingPair.getKitchenLongitude(), appetizerCookingPair.getKitchenLatitude()),
                new LocationModel(mainCookingPair.getKitchenLongitude(), mainCookingPair.getKitchenLatitude()));
        distancesForCourses.put(Courses.MAIN, calculator2.calculateDistance());
        var calculator3 = new LocationCalculatorService(
                new LocationModel(this.getKitchenLongitude(), this.getKitchenLatitude()),
                new LocationModel(mainCookingPair.getKitchenLongitude(), mainCookingPair.getKitchenLatitude()));
        distancesForCourses.put(Courses.DESSERT, calculator3.calculateDistance());
    }
    /**
     *  calculates the distance for the main cooking pair.
     * */
    private void calculateForMainCookingPair(List<PairModel> nextCookingPairs) {
        var appetizerCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.APPETIZER)
                .findFirst()
                .get();
        var dessertCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.DESSERT)
                .findFirst()
                .get();
        var calculator = new LocationCalculatorService(
                new LocationModel(appetizerCookingPair.getKitchenLongitude(), appetizerCookingPair.getKitchenLatitude()),
                new LocationModel(this.getKitchenLongitude(), this.getKitchenLatitude()));
        distancesForCourses.put(Courses.APPETIZER, calculator.calculateDistance());
        distancesForCourses.put(Courses.MAIN, calculator.calculateDistance());

        var calculator2 = new LocationCalculatorService(
                new LocationModel(this.getKitchenLongitude(), this.getKitchenLatitude()),
                new LocationModel(dessertCookingPair.getKitchenLongitude(), dessertCookingPair.getKitchenLatitude()));
        distancesForCourses.put(Courses.DESSERT, calculator2.calculateDistance());
    }
    /**
     *  calculates the distance for the appetizer cooking pair.
     * */
    private void calculateForAppetizerCookingPair(List<PairModel> nextCookingPairs) {
        var mainCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.MAIN)
                .findFirst()
                .get();
        var dessertCookingPair =  nextCookingPairs.stream()
                .filter(pair -> pair.getCookingCourse() == Courses.DESSERT)
                .findFirst()
                .get();
        var calculator = new LocationCalculatorService(
                new LocationModel(mainCookingPair.getKitchenLongitude(), mainCookingPair.getKitchenLatitude()),
                new LocationModel(this.getKitchenLongitude(), this.getKitchenLatitude()));
        distancesForCourses.put(Courses.APPETIZER, 0d);
        distancesForCourses.put(Courses.MAIN, calculator.calculateDistance());

        var calculator2 = new LocationCalculatorService(
                new LocationModel(mainCookingPair.getKitchenLongitude(), mainCookingPair.getKitchenLatitude()),
                new LocationModel(dessertCookingPair.getKitchenLongitude(), dessertCookingPair.getKitchenLatitude()));
        distancesForCourses.put(Courses.DESSERT, calculator2.calculateDistance());
    }

    /**
     * retrieves the path length of the pair.
     * */
    public double getPathLength() {
        calculatePathLength();
        for(var distance : distancesForCourses.values())
            pathLength += distance;
        return pathLength;
    }

    public Map<Courses, Double> getDistancesForCourses() {
        return distancesForCourses;
    }

    /**
     * retrieves the data of the pair in a string array format.
     * */
    public String[] getData(){
        String mainFoodPreference = this.mainFoodPreference.name();
        String course = this.cookingCourse.name();
        int courseNumberAsInt = 0;
        if(mainFoodPreference.equals("NONE"))
            mainFoodPreference = "ANY";

        courseNumberAsInt = switch (course) {
            case "APPETIZER" -> 1;
            case "MAIN" -> 2;
            case "DESSERT" -> 3;
            default -> courseNumberAsInt;
        };
        var jointRegistration = this.jointRegistration ? 2 : 1;
        return new String[]{
                participant1.getName(),
                participant2.getName(),
                String.valueOf(jointRegistration),
                String.valueOf(kitchenLongitude),
                String.valueOf(kitchenLatitude),
                mainFoodPreference,
                String.valueOf(pairNumber),
                String.valueOf(mainCourseGroupNumber),
                String.valueOf(appetizerGroupNumber),
                String.valueOf(dessertGroupNumber),
                String.valueOf(kitchenSupplier),
                String.valueOf(courseNumberAsInt),
        };

    }
}
