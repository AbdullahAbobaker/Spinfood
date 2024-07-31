package models.service;

/**
 * The DistanceCalculator interface provides a method for calculating the distance between two locations.
 * The classes that implement this interface should provide the logic for calculating the distance based on the specific requirements.
 */
public interface DistanceCalculator {
    /**
     * Calculates the distance between two locations.
     * The method should return a double representing the distance.
     *
     * @return the distance between two locations
     */
    double calculateDistance();
}
