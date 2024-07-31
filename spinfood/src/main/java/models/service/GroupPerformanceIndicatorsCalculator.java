package models.service;

import models.data.GroupModel;
import models.data.PairModel;

import java.util.List;
/**
 * This class calculates the performance indicators of the groups.
 * The performance indicators are:
 *  - Number of Groups
 *  - Number of Successor Pairs
 *  - Gender Diversity
 *  - Average Age Difference
 *  - Average Preference Deviation
 *  - Total Path Length
 *  - Average Path Length
 *  - Standard Deviation of Path Length
 * */
public class GroupPerformanceIndicatorsCalculator {
    private List<GroupModel> groups;
    private List<PairModel> successorPairs;
    private float genderDeviation;
    private int numberOfGroups;
    private int numberOfSuccessorPairs;
    private float ageDifference;
    private float preferenceDeviation;
    private double totalPathLength;
    private double averagePathLength;
    private double standardDeviationPathLength;
    public GroupPerformanceIndicatorsCalculator(List<GroupModel> groups, List<PairModel> successorPairs) {
        this.groups = groups;
        this.successorPairs = successorPairs;
    }
    /**
     *  This method calculates the performance indicators of the groups.
     * */

    public void calculatePerformanceIndicators(){
        calculateNumberOfGroups();
        calculateNumberOfSuccessorPairs();
        calculateGenderDiversity();
        calculateAgeDifference();
        calculatePreferenceDeviation();
        calculateDistanceStatistics();
    }
    /**
     *  This method calculates the number of groups.
     * */
    private void calculateNumberOfGroups(){
        numberOfGroups = groups.size();
    }
    /**
     *  This method calculates the number of successor pairs.
     * */
    private void calculateNumberOfSuccessorPairs(){
        numberOfSuccessorPairs = successorPairs.size();
    }
    /**
     *  This method calculates the gender diversity of the groups.
     * */
    private void calculateGenderDiversity(){
        if (groups.isEmpty())
            return;

        float totalDeviation = 0;
        for(GroupModel groupModel : groups){
            int numberOfWomen = groupModel.getNumberOfWomen();
            int numberOfOthers = groupModel.getNumberOfOthers();
            int totalPersons = numberOfOthers + numberOfWomen;
            float ratio = (float) numberOfWomen / totalPersons;
            float deviation = Math.abs(ratio - 0.5f);
            totalDeviation += deviation;
        }
        genderDeviation = totalDeviation / groups.size();
    }

    /**
     *  This method calculates the average age difference of the groups.
     * */
    private void calculateAgeDifference(){
        if (groups.isEmpty())
            return;

        int totalAgeDifference = 0;

        for(GroupModel group : groups){
            totalAgeDifference += group.getAgeDifference();
        }
        ageDifference = (float) totalAgeDifference / groups.size();
    }

    /**
     *  This method calculates the average preference deviation of the groups.
     * */
    private void calculatePreferenceDeviation(){
        float totalDeviation = 0;
        for(var group: groups){
            totalDeviation += group.getPreferenceDeviation();
        }
        preferenceDeviation = totalDeviation / groups.size();
    }
    /**
     *  This method calculates the statistics of the path lengths of the groups.
     * */
    private void calculateDistanceStatistics(){
        for(var group: groups){
            totalPathLength += group.getPathLength();
        }
        averagePathLength = totalPathLength / groups.size();
        float sumOfSquares = 0;
        for(var group: groups){
            sumOfSquares += (float) Math.pow(group.getPathLength() - averagePathLength, 2);
        }
        standardDeviationPathLength = Math.sqrt(sumOfSquares / groups.size());
    }
    /**
     *  This method prints the performance indicators of the groups.
     * */
    public void printDetails() {
        System.out.println("Number of Groups: " + numberOfGroups);
        System.out.println("Number of Successor Pairs: " + numberOfSuccessorPairs);
        System.out.println("Gender Deviation: " + genderDeviation);
        System.out.println("Average Age Difference: " + ageDifference);
        System.out.println("Average Preference Deviation: " + preferenceDeviation);
        System.out.println("Total Path Length: " + totalPathLength);
        System.out.println("Average Path Length: " + averagePathLength);
        System.out.println("Standard Deviation of Path Length: " + standardDeviationPathLength);
    }

}
