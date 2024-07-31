package views;

import models.enums.FoodPreferences;
import models.enums.Gender;
import models.enums.HasKitchen;

/**
 * View class for the Participant.
 * It contains methods to print the details of the participant.
 * */
public class ParticipantView {

    /**
     * @param id The ID of the participant.
     * @param name The name of the participant.
     * @param foodPreferences The food preferences of the participant.
     * @param age The age of the participant.*/
    public void printParticipantDetails(String id, String name, FoodPreferences foodPreferences, int age,
                                        Gender sex, HasKitchen hasKitchen) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("ID: ").append(id).append("\n");
        strBuilder.append("Name: ").append(name).append("\n");
        strBuilder.append("Age: ").append(age).append("\n");
        strBuilder.append("Sex: ").append(sex).append("\n");

        if (foodPreferences != null) {
            strBuilder.append("Food Preferences: ").append(foodPreferences).append("\n");
        }
        if (hasKitchen != null) {
            strBuilder.append("Has Kitchen: ").append(hasKitchen);
        }
        System.out.println(strBuilder);
    }
    /**
     * @param id The ID of the participant.
     * @param name The name of the participant.
     * @param age The age of the participant.
     * @description This method prints the details of the pair participant.
     * */
   public void printPairParticipant(String id, String name, int age) {
        if (id != null) {
            String stringBuilder = "Pair Participant: {" +
                    "ID: " + id + ", " +
                    "Name: " + name + ", " +
                    "Age: " + age + "}" + "\n";
            System.out.println(stringBuilder);
        }
        else {
            System.out.println("Pair Participant: NONE");
            System.out.println();
        }
    }
}
