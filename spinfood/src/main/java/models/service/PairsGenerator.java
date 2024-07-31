package models.service;

import models.data.PairModel;
import models.data.ParticipantModel;

import java.util.List;
/**
 * The PairsGenerator interface provides a method for generating pairs of participants for the Spinfood event.
 * The classes that implement this interface should provide the logic for generating the pairs based on the specific requirements of the event.
 */
public interface PairsGenerator {
    /**
     * Generates the pairs of participants for the Spinfood event.
     * The method should return a list of PairModel objects, each representing a pair of participants.
     *
     * @return a list of pairs of participants
     */
    List<PairModel> generatePairs();

    /**
     * Retrieves the successor pairs
     * @return a list of participants that were not paired
     */
    List<ParticipantModel> getSuccessorParticipants();
}
