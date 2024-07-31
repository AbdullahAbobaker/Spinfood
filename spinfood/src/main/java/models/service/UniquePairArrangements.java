package models.service;

import models.data.GroupModel;
import models.data.PairModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The UniquePairArrangements class is responsible for generating unique arrangements of pairs for the Spinfood event.
 * It provides methods to generate unique arrangements and validate the groups of pairs.
 * The class uses the PairModel and GroupModel classes to access the pairs and groups of participants.
 */
public class UniquePairArrangements {
    /**
     * Generates unique arrangements of pairs for the Spinfood event.
     * The method should return a list of lists of GroupModel objects, each list representing a unique arrangement of groups.
     *
     * @param pairs The list of pairs to be arranged.
     * @param numArrangements The number of unique arrangements to be generated.
     * @return a list of lists of groups of participants
     */
    public static List<List<GroupModel>> generateUniqueArrangements(List<PairModel> pairs, int numArrangements) {
        List<List<List<PairModel>>> arrangements = new ArrayList<>();
        Set<Set<PairModel>> usedCombinations = new HashSet<>();
        List<List<GroupModel>> resultListOfArrangements = new ArrayList<>();
        for (int i = 0; i < numArrangements; i++) {
            List<List<PairModel>> currentArrangement = new ArrayList<>();
            List<PairModel> availablePairs = new ArrayList<>(pairs);

            while (!availablePairs.isEmpty()) {
                boolean foundGroup = false;
                for (List<PairModel> group : combinations(availablePairs, 3)) {
                    if (isValidGroup(group, usedCombinations)) {
                        currentArrangement.add(group);
                        availablePairs.removeAll(group);
                        for (List<PairModel> pair : combinations(group, 2)) {
                            usedCombinations.add(new HashSet<>(pair));
                        }
                        foundGroup = true;
                        break;
                    }
                }
                if (!foundGroup) {
                    throw new RuntimeException("Unable to find valid group. This might happen if it's impossible to satisfy the constraints.");
                }
            }
            arrangements.add(currentArrangement);
        }
        for (List<List<PairModel>> arrangement : arrangements) {
            List<GroupModel> groups = new ArrayList<>();
            for (List<PairModel> group : arrangement) {
                groups.add(new GroupModel(group));
            }
            resultListOfArrangements.add(groups);      }

        return resultListOfArrangements;
    }

    /**
     * Validates a group of pairs.
     * The method should return a boolean indicating whether the group is valid.
     *
     * @param group The group of pairs to be validated.
     * @param usedCombinations The set of used combinations of pairs.
     * @return a boolean indicating whether the group is valid
     */
    private static boolean isValidGroup(List<PairModel> group, Set<Set<PairModel>> usedCombinations) {
        for (List<PairModel> pair : combinations(group, 2)) {
            if (usedCombinations.contains(new HashSet<>(pair))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates combinations of pairs.
     * The method should return a list of lists of PairModel objects, each list representing a combination of pairs.
     *
     * @param list The list of pairs to be combined.
     * @param k The number of pairs in each combination.
     * @return a list of lists of pairs
     */
    private static List<List<PairModel>> combinations(List<PairModel> list, int k) {
        List<List<PairModel>> result = new ArrayList<>();
        combinationsHelper(list, k, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Helper method for generating combinations of pairs.
     * The method should recursively generate combinations of pairs.
     *
     * @param list The list of pairs to be combined.
     * @param k The number of pairs in each combination.
     * @param start The starting index for the combination.
     * @param current The current combination of pairs.
     * @param result The list of combinations of pairs.
     */
    private static void combinationsHelper(List<PairModel> list, int k, int start, List<PairModel> current, List<List<PairModel>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            current.add(list.get(i));
            combinationsHelper(list, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
