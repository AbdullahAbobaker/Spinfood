package views;
/**
 * View class for the Kitchen.
 * It contains methods to print the details of the kitchen.
 * */
public class KitchenView {

    /**
     * @param story The story of the kitchen.
     * @param latitude The latitude of the kitchen location.
     * @param longitude The longitude of the kitchen location.
     * @description This method prints the details of the kitchen.
     * */
    public void  printKitchenDetails(
           final double story, final double latitude, final double longitude) {
       StringBuilder strBuilder = new StringBuilder();
       var latitudeStr = latitude == -1 ? "N/A" : String.valueOf(latitude);
       var longitudeStr = longitude == -1 ? "N/A" : String.valueOf(longitude);
       strBuilder.append("Kitchen Details: {");
       strBuilder.append("Story: ").append(story);
       strBuilder.append(" Location: ")
                 .append("{ latitude=")
                 .append(latitudeStr)
                 .append(", longitude=")
                 .append(longitudeStr)
                 .append(" }")
                 .append("}");
       System.out.println(strBuilder);
    }
}
