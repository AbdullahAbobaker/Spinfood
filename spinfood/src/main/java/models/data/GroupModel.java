package models.data;

import models.enums.Courses;
import models.enums.FoodPreferences;

import java.util.List;

/**
 * The GroupModel class represents a group of pairs for the Spinfood event.
 * It contains three pairs, the group's food preference, the course they are assigned to, the age difference within the group, and the distance to the party location.
 * The class uses the PairModel, FoodPreferences, and Courses classes to access the pairs, food preferences, and courses.
 */
public class GroupModel {
    private PairModel firstPair;
    private PairModel secondPair;
    private PairModel thirdPair;
    private FoodPreferences groupFoodPreference;
    private Courses course;
    private int ageDifference;
    private double distanceToPartyLocation;
    /**
     * Constructor for the GroupModel class.
     * @param listOfPairs The list of pairs to be grouped.
     */
    public GroupModel(List<PairModel> listOfPairs) {
        this.firstPair = listOfPairs.get(0);
        this.secondPair = listOfPairs.get(1);
        this.thirdPair = listOfPairs.get(2);

        setAgeDifference();
    }
    public FoodPreferences getGroupFoodPreference() {
        return groupFoodPreference;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public void setGroupFoodPreference(FoodPreferences groupFoodPreference) {
        this.groupFoodPreference = groupFoodPreference;
    }

    public List<PairModel> getPairs() {
        return List.of(firstPair, secondPair, thirdPair);
    }

    public void setAppetizerGroupNumber(int appetizerGroupCounter) {
        var pairs = getPairs();
        pairs.forEach(pair -> pair.setAppetizerGroupNumber(appetizerGroupCounter));
    }

    public void setMainCourseGroupNumber(int mainCourseGroupCounter) {
        var pairs = getPairs();
        pairs.forEach(pair -> pair.setMainCourseGroupNumber(mainCourseGroupCounter));
    }

    public void setDessertGroupNumber(int dessertGroupCounter) {
        var pairs = getPairs();
        pairs.forEach(pair -> pair.setDessertGroupNumber(dessertGroupCounter));
    }

    private void setAgeDifference(){
        var pairs = getPairs();
        var ageDifference = 0;
        for (PairModel pair : pairs) {
            ageDifference += pair.getAgeDifference();
        }
        this.ageDifference = ageDifference;
    }
    public int getAgeDifference() {
        return ageDifference;
    }

    public double  getDistanceToPartyLocation() {
        var pairs = getPairs();
        var distanceToPartyLocation = 0.0;
        for (PairModel pair : pairs) {
            distanceToPartyLocation += pair.getDistanceToPartyLocation();
        }
        this.distanceToPartyLocation = distanceToPartyLocation;
        return distanceToPartyLocation;
    }
    public int getNumberOfWomen(){
        int numberOfWomen = 0;
        for(var pair : getPairs()){
            numberOfWomen += pair.getNumberOfWomen();
        }
        return numberOfWomen;
    }
    public int getNumberOfOthers(){
        int numberOfOthers = 0;
        for(var pair : getPairs()){
            numberOfOthers += pair.getNumberOfOthers();
        }
        return numberOfOthers;
    }
    public float getPreferenceDeviation(){
        int totalDeviation = 0;
        for(var pair : getPairs()){
           totalDeviation += pair.getPreferenceDeviation();
        }
        return (float) totalDeviation / getPairs().size();
    }
    public double getPathLength() {
        double pathLength = 0.0;
        for (PairModel pair : getPairs()) {
            pathLength += pair.getPathLength();
        }
        return pathLength;
    }

}
