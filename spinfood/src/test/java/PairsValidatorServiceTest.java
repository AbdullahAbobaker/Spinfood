import models.enums.FoodPreferences;
import models.data.KitchenModel;
import models.data.PairModel;
import models.data.ParticipantModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.service.PairsValidatorService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The PairsValidatorServiceTest class contains unit tests for the PairsValidatorService class.
 * It tests the validate method of the PairsValidatorService class with different scenarios.
 * It includes tests for valid pairs, pairs with invalid food preferences, and pairs without kitchen availability.
 */
class PairsValidatorServiceTest {
    private PairModel validPair;
    private PairModel invalidFoodPreferencePair;
    private PairModel invalidKitchenAvailabilityPair;

    /**
     * This method sets up the test data before each test.
     * It creates several PairModel instances with different configurations.
     */
    @BeforeEach
    void setUp() {
        ParticipantModel participant1 = new ParticipantModel();
        participant1.setFoodPreferences(FoodPreferences.MEAT);
        participant1.setKitchen(new KitchenModel());

        ParticipantModel participant2 = new ParticipantModel();
        participant2.setFoodPreferences(FoodPreferences.MEAT);
        participant2.setKitchen(new KitchenModel());

        ParticipantModel participant3 = new ParticipantModel();
        participant3.setFoodPreferences(FoodPreferences.VEGAN);
        participant3.setKitchen(new KitchenModel());

        ParticipantModel participant4 = new ParticipantModel();
        participant4.setFoodPreferences(FoodPreferences.MEAT);
        participant4.setKitchen(null);

        ParticipantModel participant5 = new ParticipantModel();
        participant5.setFoodPreferences(FoodPreferences.MEAT);
        participant5.setKitchen(null);

        validPair = new PairModel(participant1, participant2);
        invalidFoodPreferencePair = new PairModel(participant1, participant3);
        invalidKitchenAvailabilityPair = new PairModel(participant4, participant5);
    }

    /**
     * This method tests the validate method of the PairsValidatorService class.
     * It checks that the validate method correctly identifies valid and invalid pairs.
     * It asserts that an IllegalArgumentException is thrown for pairs with invalid food preferences or kitchen availability.
     * It also asserts that no exception is thrown for valid pairs.
     */
    @Test
    void validate() {
        PairsValidatorService validator = new PairsValidatorService(Arrays.asList(validPair));
        validator.validate();
        assertDoesNotThrow(validator::validate);

        validator = new PairsValidatorService(Arrays.asList(invalidFoodPreferencePair));
        assertThrows(IllegalArgumentException.class, validator::validate, "Meat eater with vegan or vegetarian in pair 2");

        validator = new PairsValidatorService(Arrays.asList(invalidKitchenAvailabilityPair));
        assertThrows(IllegalArgumentException.class, validator::validate, "No kitchen available for pair 3");

        // Print completion message to console
        System.out.println("All tests completed successfully.");
    }
}
