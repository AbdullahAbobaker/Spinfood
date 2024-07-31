package controller;

import java.util.ArrayList;
import java.util.List;

import io.CSVDataReader;

import models.data.KitchenModel;
import models.data.LocationModel;
import models.data.ParticipantModel;
import models.service.RegistrationService;
import views.*;

/**
 * Controller class for the registration of participants.
 * It contains methods to register participants from a CSV file and print the details of all participants.
 * */
public class Registration {
    private List<ParticipantModel> participants = new ArrayList<>();
    private final ParticipantView pairParticipantView = new ParticipantView();
    private final ParticipantView participantView = new ParticipantView();
    private final KitchenView kitchenView = new KitchenView();
    private final CSVDataReader csvDataReader = new CSVDataReader();
    private final RegistrationService registrationService = new RegistrationService();
    private LocationModel partyLocation;
    /**
     * @param filePath The path to the CSV file containing participant data.
     * @description This method reads the participants from a CSV file and processes them using the registration service.
     * It reads the data from the CSV file using the readData method of the CSVDataReader class.
     * The raw data is then processed using the processParticipants method of the registration service.
     * The resulting participants are stored in the participants list.
     * */
    public void registerParticipants(String filePath) {
        List<String[]> rawData = csvDataReader.readData(filePath);
        participants = registrationService.processParticipants(rawData);
    }

    public void registerPartyLocation(String filePath) {
        List<String[]> rawData = csvDataReader.readData(filePath);
        partyLocation = registrationService.processPartyLocation(rawData);
    }
    /**
     * @description This method prints the details of all participants.
     * It iterates over the participants list and prints the details of each participant using the printParticipantDetails method of the participant view.
     * If the participant has a kitchen, the kitchen details are also printed using the printKitchenDetails method of the kitchen view.
     * If the participant has a pair participant, the pair participant details are also printed using the printPairParticipant method of the participant view.
     * */

    public void printParticipants() {
        if (participants.isEmpty()) {
            System.out.print("No participants added yet.");
            return;
        }
        participants.forEach(participant -> {
            participantView.printParticipantDetails(
                    participant.getID(),
                    participant.getName(),
                    participant.getFoodPreferences(),
                    participant.getAge(),
                    participant.getSex(),
                    participant.getHasKitchen());
            printParticipantKitchenDetails(participant);
            printParticipantPairDetails(participant);
        });
    }

    /**
     *  @return List<ParticipantModel>
     *  @description This method returns the list of participants.
     * */
    public List<ParticipantModel> getParticipants(){
        return participants;
    }


    /**
     * @param participant participant The participant whose pair participant details are to be printed.
     * @description This method prints the details of the pair participant of a given participant.
     * It takes a participant object as input and gets the pair participant using the getPairParticipants method.
     * If the pair participant is not null, the details of the pair participant are printed using the printPairParticipant method of the participant view.
     * Otherwise, the method prints null values for the pair participant.
     * */
    private void printParticipantPairDetails(ParticipantModel participant) {
        ParticipantModel pair = participant.getPairParticipants();
        if (pair != null)
            pairParticipantView.printPairParticipant(pair.getID(), pair.getName(), pair.getAge());
        else
            pairParticipantView.printPairParticipant(null, null, -1);
    }

    /**
     *  @param participant The participant whose kitchen details are to be printed.
     * @description This method prints the details of the kitchen of a given participant.
     * It takes a participant object as input and gets the kitchen using the getKitchen method.
     * If the kitchen is not null, the details of the kitchen are printed using the printKitchenDetails method of the kitchen view.
     *
     * */
    private void printParticipantKitchenDetails(ParticipantModel participant) {
        KitchenModel kitchen = participant.getKitchen();
        var  location = kitchen.getLocation();
        var latitude =  location == null ? -1 : location.getLatitude();
        var longitude =  location == null ? -1 : location.getLongitude();
        kitchenView.printKitchenDetails(kitchen.getStory(), latitude,longitude);
    }

    public LocationModel getPartyLocation() {
        return partyLocation;
    }
}
