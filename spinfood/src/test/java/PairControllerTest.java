import controller.Registration;
import models.data.KitchenModel;
import models.data.LocationModel;
import models.data.PairModel;
import models.data.ParticipantModel;
import models.enums.FoodPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.service.PairGeneratorService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * This class contains unit tests for the functionality provided by the PairController class.
 * It tests various scenarios related to generating pairs of participants for an event.
 */

public class PairControllerTest {


    Registration reg;
    ParticipantModel participant1;
    ParticipantModel participant2;
    ParticipantModel participant3;
    ParticipantModel participant4;
    ParticipantModel participantFleischi;
    ParticipantModel participantVegan;
    ParticipantModel participantVeggie;





    /**
     * Set up method executed before each test method to initialize test data and dependencies.
     */
    @BeforeEach
    public void setUp() {
        reg = new Registration();
        try {
            reg.registerParticipants("src/Data/teilnehmerliste.csv");
            reg.registerPartyLocation("src/Data/partylocation.csv");

        } catch (Exception e) {
            // Print the exception details for debugging
            fail("Error reading participants from CSV file: " + e.getMessage());
        }

        participant1 = (reg.getParticipants().get(0));
        participant2 = (reg.getParticipants().get(1));
        participant3 = (reg.getParticipants().get(2));
        participant4 = (reg.getParticipants().get(3));
        participantFleischi = reg.getParticipants().get(4);
        participantVegan = reg.getParticipants().get(9);
        participantVeggie = reg.getParticipants().get(10);


    }

    /**
     * Test method to verify that participants with incompatible food preferences are not paired.
     */
    @Test
    public void testFoodIncompatibility() {
        List<ParticipantModel> participants = List.of(participantFleischi, participantVegan);
        PairGeneratorService pairGeneratorService = new PairGeneratorService(participants, reg.getPartyLocation());
        List<PairModel> pairs = pairGeneratorService.generatePairs();
        assertTrue(pairs.isEmpty());
    }

    @Test
    public void testFoodCompatibility() {
        PairGeneratorService pairGeneratorService = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        List<PairModel> pairs = pairGeneratorService.generatePairs();

        boolean allPairsValid = pairs.stream().allMatch(pair -> isCompatible(pair.getParticipant1().getFoodPreferences(), pair.getParticipant2().getFoodPreferences()));

        assertTrue(allPairsValid);
    }
    public boolean isCompatible(FoodPreferences first, FoodPreferences second) {
        // If both participants have the same food preference, they are compatible
        if (first == second) {
            return true;
        }

        // If one participant is MEAT and the other is VEGAN, they are not compatible
        return (first != FoodPreferences.MEAT || second != FoodPreferences.VEGAN) &&
                (first != FoodPreferences.VEGAN || second != FoodPreferences.MEAT);
    }
    @Test
    public void testMeatEatersWithAnyFoodEater(){
        var service = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        var pairs = service.generatePairs();

       var result = pairs.stream()
                .filter(p -> p.getMainFoodPreference() == FoodPreferences.MEAT && !p.isJointRegistration())
                .allMatch(p-> isValidFoodPreferenceForMeatAndAny(p.getParticipant1().getFoodPreferences(),
                        p.getParticipant2().getFoodPreferences()) );
       assertTrue(result);
    }

    @Test
    public void testVeggieAndVeganerFoodPreference(){
        var service = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        var pairs = service.generatePairs();

        var result = pairs.stream()
                .filter(p -> (p.getParticipant1().getFoodPreferences() == FoodPreferences.VEGAN &&
                        p.getParticipant2().getFoodPreferences() == FoodPreferences.VEGGIE) ||
                        (p.getParticipant2().getFoodPreferences() == FoodPreferences.VEGAN &&
                                p.getParticipant2().getFoodPreferences() == FoodPreferences.VEGGIE))
                .allMatch(p ->p.getMainFoodPreference() ==FoodPreferences.VEGAN);
        assertTrue(result);
    }

    public boolean isValidFoodPreferenceForMeatAndAny(FoodPreferences first, FoodPreferences second){
        if(first == FoodPreferences.MEAT && second == FoodPreferences.MEAT)
            return true;
        if (first == FoodPreferences.MEAT)
            return second == FoodPreferences.NONE;

        if(second == FoodPreferences.MEAT)
            return first == FoodPreferences.NONE;

        return false;
    }

    @Test
    public void testPairsWithHighKitchenOccupation(){
        PairGeneratorService pairGeneratorService = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        List<PairModel> pairs = pairGeneratorService.generatePairs();
        Map<Double, Long> kitchenOccupation = pairs.stream()
                .collect(Collectors.groupingBy(PairModel::getKitchenLatitude, Collectors.counting()));
        boolean allPairsValid = pairs.stream().allMatch(pair -> kitchenOccupation.get(pair.getKitchenLatitude()) <= 3);
        assertTrue(allPairsValid);

    }
    @Test
    public void testKitchenAvailabilty(){
        PairGeneratorService pairGeneratorService = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        List<PairModel> pairs = pairGeneratorService.generatePairs();

        var res = pairs.stream().allMatch(p-> p.getKitchenLatitude() !=-1 &&p.getKitchenLongitude() !=-1);
        assertTrue(res);
    }
    @Test
    public void testPairDistanceIsNotZero() {
        PairGeneratorService pairGeneratorService = new PairGeneratorService(reg.getParticipants(), reg.getPartyLocation());
        List<PairModel> pairs = pairGeneratorService.generatePairs();

        boolean allPairsValid = pairs.stream().allMatch(pair -> {
            KitchenModel kitchen1 = pair.getParticipant1().getKitchen();
            KitchenModel kitchen2 = pair.getParticipant2().getKitchen();

            if (kitchen1 == null || kitchen2 == null || kitchen1.getLocation() == null || kitchen2.getLocation() == null) {
                return true;
            }
            LocationModel loc1 = kitchen1.getLocation();
            LocationModel loc2 = kitchen2.getLocation();
            return !(loc1.getLatitude() == loc2.getLatitude() && loc1.getLongitude() == loc2.getLongitude());
        });

        assertTrue(allPairsValid);
    }



}

