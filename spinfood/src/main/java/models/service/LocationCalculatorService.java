package models.service;

import models.data.LocationModel;
/**
 * LocationCalculatorService class
 * Service class for calculating the distance between two locations
 */
public class LocationCalculatorService implements DistanceCalculator{
    private LocationModel location1;
    private LocationModel location2;

    /**
     * Constructor for the LocationCalculatorService class
     * @param location1 The first location
     * @param location2 The second location
     */
    public LocationCalculatorService(LocationModel location1, LocationModel location2) {
        this.location1 = location1;
        this.location2 = location2;
    }

    /**
     * Calculates the distance between two locations
     * @return The distance between the two locations
     */

    @Override
    public double calculateDistance() {
        final int R = 6371; // Radius of the earth in km

        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c; // Distance in km

        return distance;
    }

}
