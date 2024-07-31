package models.data;
/**
 * Model class for the Location.
 * It contains the longitude and latitude of the location.
 * */

public class LocationModel {
    private final double  longitude;
    private final double latitude;

    /**
     * @param locationLongtiutde The locationLongtiutde of the location.
     * @param locationLatitude The locationLatitude of the location.
     * @description This method sets the locationLongtiutde
     * and locationLatitude of the location.
     * */

    public LocationModel(final double locationLongtiutde,
                         final double locationLatitude) {
        this.longitude = locationLongtiutde;
        this.latitude = locationLatitude;
    }


    /**
     *  @return double
     *  @description This method returns the longitude of the location.
     * */
    public double getLongitude() {
        return longitude;
    }
    /**
     *  @return double
     *  @description This method returns the latitude of the location.
     * */
    public double getLatitude() {
        return latitude;
    }
}
