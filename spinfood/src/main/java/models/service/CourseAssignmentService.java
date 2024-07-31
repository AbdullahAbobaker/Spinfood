package models.service;
import models.enums.Courses;
import models.data.GroupModel;
import models.data.LocationModel;
import models.data.PairModel;

import java.util.*;

/**
 * The CourseAssignmentService class is responsible for assigning cooking courses to pairs of participants.
 * It uses a list of PairModel objects, a LocationModel object, and a list of GroupModel objects to assign the courses.
 * The assignment is based on the distance of each pair's kitchen to the party location.
 * The pairs in the last arrangement are sorted in descending order of their distance to the party location.
 * Each pair in the last arrangement is then assigned a course based on its position in the sorted list.
 * The pair is also marked as cooking.
 */
public class CourseAssignmentService {
    private static LocationCalculatorService locationCalculatorService;

    /**
     * Assigns cooking courses to the pairs in a cluster.
     * The assignment is based on the distance of each pair's kitchen to the party location.
     * The pairs in the last arrangement are sorted in descending order of their distance to the party location.
     * Each pair in the last arrangement is then assigned a course based on its position in the sorted list.
     * The pair is also marked as cooking.
     *
     * @param cluster the list of pairs
     * @param partyLocation the location of the party
     * @param lastArrangement the list of groups in the last arrangement
     */
    public static void assignCourses(List<PairModel> cluster,LocationModel partyLocation, List<GroupModel> lastArrangement) {
        List<PairModel> pairs = new ArrayList<>(cluster);
        pairs.forEach(pair -> {
            locationCalculatorService = new LocationCalculatorService(new LocationModel(pair.getKitchenLatitude(), pair.getKitchenLongitude()),partyLocation);
            pair.setDistanceToPartyLocation(locationCalculatorService.calculateDistance());
        });
        lastArrangement.sort(Comparator.comparing(GroupModel::getDistanceToPartyLocation).reversed());
        for(int i = 0; i < lastArrangement.size(); i++) {
            for(var pair : pairs) {
                if(lastArrangement.get(i).getPairs().contains(pair)) {
                    pair.setCookingCourse(Courses.values()[i]);
                    pair.setCooking(true);
                }
            }

        }
    }
}
