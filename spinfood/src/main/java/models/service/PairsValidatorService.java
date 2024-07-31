package models.service;

import models.enums.FoodPreferences;
import models.data.PairModel;

import java.util.List;

/**
 * The PairsValidatorService class is responsible for validating the pairs of participants for the Spinfood event.
 * It provides methods to validate the main food preference and kitchen availability of each pair.
 * The class uses the PairModel and FoodPreferences classes to access the food preferences and kitchen availability of the participants.
 */
public class PairsValidatorService {
    List<PairModel> pairModels;

    /**
     * Constructor for the PairsValidatorService class.
     * @param pairModels The pairs of participants to be validated.
     */
    public PairsValidatorService(List<PairModel> pairModels) {
        this.pairModels = pairModels;
    }

    /**
     * Validates the main food preference of each pair.
     * The method throws an IllegalArgumentException if a pair consists of a meat eater and a vegan or vegetarian.
     */
    private void validateMainFoodPreference() {
        //no meat eater with vegetarian or vegan
        for (PairModel pairModel : pairModels) {
            if (pairModel.getParticipant1().getFoodPreferences() == FoodPreferences.MEAT &&
                    (pairModel.getParticipant2().getFoodPreferences() == FoodPreferences.VEGGIE
                            || pairModel.getParticipant2().getFoodPreferences() == FoodPreferences.VEGAN)) {
                throw new IllegalArgumentException("Meat eater with vegan or vegetarian in pair " + pairModel.getPairNumber());
            }
        }
    }

    /**
     * Validates the kitchen availability of each pair.
     * The method throws an IllegalArgumentException if a pair does not have a kitchen available.
     */
    private void validateKitchenAvailability() {
        //each pair should have a kitchen available

        for (PairModel pairModel : pairModels) {
            if (pairModel.getParticipant1().getKitchen() == null && pairModel.getParticipant2().getKitchen() == null) {
                throw new IllegalArgumentException("No kitchen available for pair " + pairModel.getPairNumber());
            }
        }
    }

    /**
     * Validates the pairs of participants.
     * The method calls the validateMainFoodPreference and validateKitchenAvailability methods to validate the pairs.
     */
    public void validate(){
        validateMainFoodPreference();
        validateKitchenAvailability();
    }
}