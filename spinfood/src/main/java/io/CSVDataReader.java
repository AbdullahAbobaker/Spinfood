package io;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVDataReader {
    /**
     *  The logger for the CSVDataReader class.
     * */
    private static final Logger LOGGER = Logger.getLogger(
            CSVDataReader.class.getName());

    /**
     * @param filePath The path to the CSV file containing participant data.
     * @return A list of String arrays representing the
     * data read from the CSV file.
     * @description This method reads the participants from CSV file
     * and returns them as a list of strings.
     * The method reads the file using the CSVReader class and skips the header.
     * It then reads all records and convert them to participants using
     * the createParticipantFromCSVRecord method.
     * The new participants are then added to the existing list.
     * If an exception occurs, it is logged instead of being
     * printed to the console.
     */
    public List<String[]> readData(final String filePath) {
        try (FileReader fileReader = new FileReader(filePath);
             CSVReader csvReader = new CSVReader(fileReader)) {

            // Skip header
            csvReader.readNext();
            return csvReader.readAll();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "An error occurred while reading data from CSV file: "
                            + filePath, e);
            return null;
        }
    }
}
