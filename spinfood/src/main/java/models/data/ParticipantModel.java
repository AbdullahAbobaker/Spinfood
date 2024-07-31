package models.data;

import models.enums.FoodPreferences;
import models.enums.Gender;
import models.enums.HasKitchen;


/**
 *  Model class for the Participant.
 *  It contains the details of the participant such as ID, name, food
 *  preferences and age.
 * */
public class ParticipantModel {
    private String id;
    private String name;
    private FoodPreferences foodPreferences;
    private int age;
    private Gender sex;
    private HasKitchen hasKitchen;
    private KitchenModel kitchen;
    private ParticipantModel pairParticipant;
    private int ageRange;
    /**
     * @return String
     * @description This method returns the name of the participant.
     * */
    public String getName() {
        return name;
    }
    /**
     * @return age
     * @description This method returns the age of the participant.
     * */

    public int getAge() {
        return age;
    }

    /**
     * @return Gender
     * @description This method returns the Gender of the participant.
     * */
    public Gender getSex() {
        return sex;
    }

    /**
     * @description This method sets the ID of the participant.
     * */
    public void setID(String participantId) {
        this.id = participantId;
    }
    /**
     * @description This method sets the name of the participant.
     * */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @param foodPreferences The food preferences of the participant.
     * @description This method sets the food preferences of the participant.
     * */
    public void setFoodPreferences(FoodPreferences foodPreferences) {
        this.foodPreferences = foodPreferences;
    }
    /**
     * @param age The age of the participant.
     * @description This method sets the age of the participant.
     * */
    public void setAge(int age) {
        this.age = age;
    }
    /**
     * @param sex The gender of the participant.
     * @description This method sets the sex of the participant.
     * */
    public void setSex(Gender sex) {
        this.sex = sex;
    }

    /**
     * @param hasKitchen The kitchen availability of the participant.
     * @description This method sets the kitchen availability of the participant.
     * */
    public void setHasKitchen(HasKitchen hasKitchen) {
        this.hasKitchen = hasKitchen;
    }
    /**
     * @param kitchen The kitchen details of the participant.
     * @description This method sets the kitchen details of the participant.
     * */
    public void setKitchen(KitchenModel  kitchen) {
        this.kitchen = kitchen;
    }
    /**
     * @param pairParticipants The pair participant details of the participant.
     * @description This method sets the pair participant details of the participant.
     * */
    public void setPairParticipants(ParticipantModel pairParticipants) {
        this.pairParticipant = pairParticipants;
    }
    /**
     * @return String
     * @description This method returns the ID of the participant.
     * */

    public String getID() {
        return id;
    }
    /**
     * @return FoodPreferences
     * @description This method returns the food preferences of the participant.
     * */
    public FoodPreferences getFoodPreferences() {
        return foodPreferences;
    }

    /**
     * @return HasKitchen
     * @description This method returns the kitchen availability of the participant.
     * */
    public HasKitchen getHasKitchen() {
        return hasKitchen;
    }
    /**
     * @return KitchenModel
     * @description This method returns the kitchen details of the participant.
     * */
    public KitchenModel getKitchen() {
        return kitchen;
    }
    /**
     * @return ParticipantModel
     * @description This method returns the pair participant details of the participant.
     * */
    public ParticipantModel getPairParticipants() {
        return pairParticipant;
    }

    public int getAgeRange() {
        setAgeRange();
        return ageRange;
    }

    private void setAgeRange(){
        if (age >= 0 && age <= 17) {
            ageRange = 0;
        } else if (age >= 18 && age <= 23) {
            ageRange = 1;
        } else if (age >= 24 && age <= 27) {
            ageRange = 2;
        } else if (age >= 28 && age <= 30) {
            ageRange = 3;
        } else if (age >= 31 && age <= 35) {
            ageRange = 4;
        } else if (age >= 36 && age <= 41) {
            ageRange = 5;
        } else if (age >= 42 && age <= 46) {
            ageRange = 6;
        } else if (age >= 47 && age <= 56) {
            ageRange = 7;
        } else if (age >= 57) {
            ageRange = 8;
        } else {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
    }


}
