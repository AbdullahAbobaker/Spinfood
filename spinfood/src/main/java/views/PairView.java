package views;

import models.data.PairModel;

import java.util.List;

/**
 *  The PairView class is responsible for displaying the details of a pair of participants.
 *  It provides methods to print the details of a single pair or a list of pairs to the console
 *  The class uses the PairModel class to access the details of the pair.
 *  The printPair method prints the details of a single pair, while the printPairs method prints the details of a list of pairs.
 *  The PairView class is used by the PairController class to display the details of the pairs.
 * */
public class PairView {
    private PairModel pair;
    private List<PairModel> pairs;
    public PairView(PairModel pair) {
        this.pair = pair;
    }
    public PairView(List<PairModel> pairs) {
        this.pairs = pairs;
    }

    /**
     *  Prints the details of a single pair to the console.
     * */
    public void printPair() {
        System.out.println("First participant: " + pair.getParticipant1().getName());
        System.out.println("Second participant: " + pair.getParticipant2().getName());
        System.out.println("Joint registration: " + pair.isJointRegistration());
        System.out.println("Kitchen longitude: " + pair.getKitchenLongitude());
        System.out.println("Kitchen latitude: " + pair.getKitchenLatitude());
        System.out.println("Food preference: " + pair.getMainFoodPreference().name());
        System.out.println("Pair number: " + pair.getPairNumber());
        System.out.println("Appetizer group number: " + pair.getAppetizerGroupNumber());
        System.out.println("Main course group number: " + pair.getMainCourseGroupNumber());
        System.out.println("Dessert group number: " + pair.getDessertGroupNumber());
        System.out.println("Kitchen supplier: " + pair.isKitchenSupplier());
        System.out.println("Cooking course: " + pair.getCookingCourse());
        System.out.println();
    }
    public void printPairs(){
        for(var pair: pairs){
            var view = new PairView(pair);
            view.printPair();
        }

    }

}
