package models.service;

import models.data.PairModel;
import models.data.ParticipantModel;

import java.util.List;
/**
 *  This class calculates the performance indicators of the pairs.
 *  The performance indicators are:
 *  - Number of Pairs
 *  - Number of Successor Participants
 *  - Gender Diversity
 *  - Average Age Difference
 *  - Average Preference Deviation
 * */
public class PairsPerformanceIndicatorsCalculator {
    private List<PairModel> pairs;
    private List<ParticipantModel> successorPairs;
    private int numberOfPairs;
    private int numberOfSuccessorPairs;
    private float genderDiversity;
    private float ageDifference;
    private float preferenceDeviation;


    public PairsPerformanceIndicatorsCalculator(List<PairModel> pairs, List<ParticipantModel> successorPairs) {
        this.pairs = pairs;
        this.successorPairs = successorPairs;
    }
    /**
     * This method calculates the performance indicators of the pairs.
     * */
    public void calculatePerformanceIndicators() {
        calculateNumberOfPairs();
        calculateNumberOfSuccessorPairs();
        calculateGenderDiversity();
        calculateAgeDifference();
        calculatePreferenceDeviation();
    }
    /**
     *  This method calculates the number of pairs.
     * */
    private void calculateNumberOfPairs() {
        numberOfPairs = pairs.size();
    }
    /**
     *  This method calculates the number of successor participants.
     * */
    private void calculateNumberOfSuccessorPairs() {
        numberOfSuccessorPairs = successorPairs.size();
    }

    /**
     *  This method calculates the gender diversity
     * */
    private void calculateGenderDiversity() {
        float totalDeviation = 0;

        for (PairModel pair : pairs) {
            int numberOfWomen = pair.getNumberOfWomen();
            int numberOfOthers = pair.getNumberOfOthers();
            int totalPersons = numberOfWomen + numberOfOthers;

            if (totalPersons > 0) {
                float ratio = (float) numberOfWomen / totalPersons;
                float deviation = Math.abs(ratio - 0.5f);
                totalDeviation += deviation;
            }
        }

        if (!pairs.isEmpty()) {
            genderDiversity = totalDeviation / pairs.size();
        } else {
            genderDiversity = 0;
        }
    }
    /**
     *  This method calculates the average age difference.
     * */
    private void calculateAgeDifference() {
        int totalAgeDifference = 0;

        for (PairModel pair : pairs) {
            totalAgeDifference += pair.getAgeDifference();
        }

        if (!pairs.isEmpty()) {
            ageDifference = (float) totalAgeDifference / pairs.size();
        } else {
            ageDifference = 0;
        }
    }
    private void calculatePreferenceDeviation(){
        int totalDeviation = 0;
        for(PairModel pair : pairs){
            totalDeviation += pair.getPreferenceDeviation();
        }
        if (!pairs.isEmpty()) {
            preferenceDeviation = (float) totalDeviation / pairs.size();
        } else {
            preferenceDeviation = 0;
        }
    }
    /**
     *  This method prints the results of the performance indicators.
     * */
    public void printResults() {
        System.out.println("Number of Pairs: " + numberOfPairs);
        System.out.println("Number of Successor Participants: " + numberOfSuccessorPairs);
        System.out.println("Gender Diversity: " + genderDiversity);
        System.out.println("Average Age Difference: " + ageDifference);
        System.out.println("Average Preference Deviation: " + preferenceDeviation);
    }

}
