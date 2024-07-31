package models.service;

import models.data.GroupModel;
import models.data.PairModel;

import java.util.List;

/**
 * The GroupsGenerator interface provides a method for generating groups of participants for the Spinfood event.
 * The classes that implement this interface should provide the logic for generating the groups based on the specific requirements of the event.
 */
public interface GroupsGenerator {
    /**
     * Generates the groups of participants for the Spinfood event.
     * The method should return a list of GroupModel objects, each representing a group of participants.
     *
     * @return a list of groups of participants
     */
    List<GroupModel> generateGroups();

    /**
     * Gets the successor pairs for the generated groups.
     * The method should return a list of PairModel objects, each representing a pair of participants that are successors.
     *
     * @return a list of successor pairs
     */
    List<PairModel> getSuccessorPairs();
}
