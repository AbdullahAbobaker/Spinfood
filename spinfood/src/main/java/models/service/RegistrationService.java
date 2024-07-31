package models.service;

import models.enums.FoodPreferences;
import models.enums.Gender;
import models.enums.HasKitchen;
import models.data.KitchenModel;
import models.data.LocationModel;
import models.data.ParticipantModel;

import java.util.List;

/**
 * Service class for registration.
 * It processes the participants from the raw data.
 * */
public class RegistrationService {
        /**
         * @param rawData The raw data containing participant information.
         * @return List<ParticipantModel>
         * @description This method processes the participants from the raw data.
         * It takes the raw data and converts it to a list of participants using the createParticipantFromCSVRecord method.
         * The method uses the stream API to convert the raw data to a stream and then maps each record to a participant.
         * The resulting list of participants is then returned.
         * */
        public List<ParticipantModel> processParticipants(List<String[]> rawData) {
                return rawData.stream().map(this::createParticipantFromCSVRecord).toList();
        }

        public LocationModel processPartyLocation(List<String[]> rawData) {
                return new LocationModel(Double.parseDouble(rawData.get(0)[0]), Double.parseDouble(rawData.get(0)[1]));
        }

        /**
         * @param record The CSV record containing participant information.
         * @return ParticipantModel
         * @description This method creates a participant from a CSV record.
         * It takes the record and extracts the values for the participant's attributes.
         * The attributes are then set on the participant object and returned.
         * The method also creates a kitchen and a pair participant if the values are present in the record.
         * The kitchen is created using the createKitchenFromCSVRecord method and the pair participant is created using the createPairParticipantFromCSVRecord method.
         * The kitchen and pair participant are then set on the participant object.
         * */
        private ParticipantModel createParticipantFromCSVRecord(String[] record) {
                ParticipantModel participant = new ParticipantModel();

                participant.setID(record[1]);
                participant.setName(record[2]);
                participant.setFoodPreferences(FoodPreferences.valueOf(record[3].toUpperCase()));
                participant.setAge(Integer.parseInt(record[4]));
                participant.setSex(Gender.valueOf(record[5].toUpperCase()));
                participant.setHasKitchen(HasKitchen.valueOf(record[6].toUpperCase()));

                KitchenModel kitchen = createKitchenFromCSVRecord(record);
                participant.setKitchen(kitchen);

                ParticipantModel pair = createPairParticipantFromCSVRecord(record);
                participant.setPairParticipants(pair);

                return participant;
        }
        /**
         * @param record The CSV record containing participant information
         * @return KitchenModel
         * @description This method creates a kitchen from a CSV record.
         * It takes the record and extracts the values for the kitchen's attributes.
         * The attributes are then set on the kitchen object and returned.
         * If the story number is empty, it is set to 0.
         * If the latitude and longitude are empty, they are set to -1.
         * Otherwise, the values are parsed to double and set on the location object.
         * The location object is then set on the kitchen object.
         *
         * */

        private KitchenModel createKitchenFromCSVRecord(String[] record) {
                HasKitchen hasKitchen = HasKitchen.valueOf(record[6].toUpperCase());
                String kitchenStory = record[7];
                String kitchenLongitude = record[8];
                String kitchenLatitude = record[9];

                KitchenModel kitchen = new KitchenModel();

                int storyNumber = kitchenStory.isEmpty() ? 0 : (int) Float.parseFloat(kitchenStory);
                kitchen.setStory(storyNumber);

                if(hasKitchen != HasKitchen.NO)
                        kitchen.setLocation(new LocationModel(Double.parseDouble(kitchenLongitude),Double.parseDouble(kitchenLatitude)));

                return kitchen;
        }
        /**
         * @param record The CSV record containing pair participant information
         * @return ParticipantModel
         * @description This method creates a pair participant from a CSV record.
         * It takes the record and extracts the values for the pair participant's attributes.
         * The attributes are then set on the pair participant object and returned.
         * If the pair ID is empty, the method returns null.
         * Otherwise, it creates a new pair participant object and sets the
         *
         * */
        private ParticipantModel createPairParticipantFromCSVRecord(String[] record) {
                String pairId = record[10];

                if (!pairId.isEmpty()) {
                        ParticipantModel pair = new ParticipantModel();
                        pair.setID(pairId);
                        pair.setName(record[11]);
                        pair.setAge((int) Double.parseDouble(record[12]));
                        pair.setSex(Gender.valueOf(record[13].toUpperCase()));
                        return pair;
                }
                return null;
        }


}
