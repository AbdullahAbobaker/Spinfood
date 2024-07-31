package models.data;

/**
 * Model class for the Kitchen.
 * It contains the story and location of the kitchen.
 * */

public class KitchenModel {
    private double story;
    private LocationModel location;

    /**
     *  @return double
     *  @description This method returns the story of the kitchen.
     * */
    public double getStory() {
        return story;
    }
    /**
     *  @return LocationModel
     *  @description This method returns the location of the kitchen.
     * */

    public LocationModel getLocation() {
        return location;
    }
    /**
     * @param kitchenStory The kitchenStory of the kitchen.
     * @description This method sets the kitchenStory of the kitchen.
     * */

    public void setStory(final double kitchenStory) {
        story = kitchenStory;
    }
    /**
     * @param kitchenLocation The kitchenLocation of the kitchen.
     * @description This method sets the kitchenLocation of the kitchen.
     * */
    public void setLocation(final LocationModel kitchenLocation) {
        location = kitchenLocation;
    }

}
