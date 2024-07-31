
import controller.*;
import models.data.KitchenModel;
import models.data.LocationModel;
import models.data.ParticipantModel;
import models.enums.FoodPreferences;
import models.enums.Gender;
import models.enums.HasKitchen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.KitchenView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Registration class.
 */
class RegistrationTest {


    Registration registration;

    Registration reg;
    Registration reg1;




    /**
     * Setup method to initialize test objects before each test method.
     */
    @BeforeEach
    public void setUp() {
        reg = new Registration();
        reg1  = new Registration();
        try {
            reg.registerParticipants("src/Data/teilnehmerliste.csv");

        } catch (Exception e) {
            // Print the exception details for debugging
            fail("Error reading participants from CSV file: " + e.getMessage());
        }
    }

    /**
     * Test method to verify reading participants from a valid CSV file.
     *
     */
    @Test
    public void testRegister_FromFile_ValidFile() {
        assertEquals(237, reg.getParticipants().size());

        // Verify participant 1 details
        ParticipantModel participant1 = reg.getParticipants().get(0);
        assertEquals("004670cb-47f5-40a4-87d8-5276c18616ec", participant1.getID());
        assertEquals("Person1", participant1.getName());
        assertEquals(FoodPreferences.VEGGIE, participant1.getFoodPreferences());
        assertEquals(21, participant1.getAge());
        assertEquals(Gender.MALE, participant1.getSex());
        assertEquals(HasKitchen.MAYBE, participant1.getHasKitchen());

        // Verify kitchen details for participant 1
        KitchenModel kitchen = participant1.getKitchen();
        assertNotNull(kitchen);
        assertEquals(3.0, kitchen.getStory());
        assertEquals(50.594128271555807, kitchen.getLocation().getLatitude(), 0.001);
        assertEquals(8.673368271555807, kitchen.getLocation().getLongitude(), 0.001);

        // Verify pair participant details for participant 1
        ParticipantModel pairParticipant2 = reg.getParticipants().get(1);
        assertNotNull(pairParticipant2);
        assertEquals("01a099db-22e1-4fc3-bbf5-db738bc2c10b", pairParticipant2.getID());
        assertEquals("Person2", pairParticipant2.getName());
        assertEquals(26, pairParticipant2.getAge());
        assertEquals(Gender.MALE, pairParticipant2.getSex());
    }

    /**
     * Test method to verify reading participants from an invalid CSV file.
     */
    @Test
    public void testRegister_FromFile_InvalidFile() {
        // Use a non-existent file path
        String invalidFilePath = "invalid_file.csv";

        // Expect an exception when reading from an invalid file
        assertThrows(Exception.class, () -> registration.registerParticipants(invalidFilePath));
    }

    /**
     * Test method to print participants.
     */
    @Test
    public void testPrintParticipants() {
        reg.printParticipants();
    }

    /**
     * Test method to verify reading participants from an empty CSV file.
     *
     */
    @Test
    public void testRegister_FromFile_EmptyFile() {
        // Read participants from the empty file
        String emptyFilePath = "src/Data/test_participants.csv";
        reg1.registerParticipants(emptyFilePath);

        // Verify that no participants are added
        assertTrue(reg1.getParticipants().isEmpty());
    }

    /**
     * Test case to verify the behavior of the printParticipants() method when the participants list is empty.
     * It ensures that the method prints "No participants added yet." when the list is empty.
     */
    @Test
    public void printParticipants_EmptyList() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Call the method
        Registration registration = new Registration();
        registration.printParticipants();

        assertEquals("No participants added yet.", outputStreamCaptor.toString());
    }


    /**
     * Test case to verify the behavior of the printParticipantKitchenDetails() method.
     * It ensures that the method correctly prints kitchen details to the standard output stream.
     * This test verifies the output format and content.
     */
    @Test
    public void printParticipantKitchenDetails() {

        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create a KitchenModel instance
        KitchenModel kitchen = new KitchenModel();
        kitchen.setStory(3.0);

        // Create a Location instance
        LocationModel location = new LocationModel(50.5941282715558, 8.673368271555807);

        // Set the Location to the KitchenModel
        kitchen.setLocation(location);

        // Create a KitchenView instance
        KitchenView kitchenView = new KitchenView();

        // Call the method
        kitchenView.printKitchenDetails(kitchen.getStory(), location.getLatitude(), location.getLongitude());

        // Trim and get the actual and expected output
        String actualOutput = outputStreamCaptor.toString().trim();
        String expectedOutput = "Kitchen Details: {Story: 3.0 Location: { latitude=8.673368271555807, longitude=50.5941282715558 }}";

        // Verify the output
        assertEquals(expectedOutput, actualOutput);
    }


}
