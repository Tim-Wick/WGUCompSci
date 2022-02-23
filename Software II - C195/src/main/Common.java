package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jdbc.JDBC;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Common {
    /**
     * Changes scenes based on a file name and button performing the action
     * @param fileName
     * @param button
     * @throws IOException
     */
    public static void changeScene(String fileName, Button button) throws IOException {
        Parent root = FXMLLoader.load(Common.class.getResource("/view/" + fileName + ".fxml"));
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    /**
     * Throws an error
     * @param errorMessage
     */
    public static void throwError(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(errorMessage);
        errorAlert.showAndWait();
    }

    /**
     * Throws an error asking user for confirmation
     * @param confirmation
     * @return
     */
    public static boolean getConfirmation(String confirmation) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText(confirmation);
        Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
        return confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK;
    }

    /**
     * Gets an ObservableList of countries
     * @return countryList
     * @throws SQLException
     */
    public static ObservableList<String> getCountries() throws SQLException {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        ResultSet countryResults = JDBC.searchDB("select Country from countries");
        while (countryResults.next()) {
            countryList.add(countryResults.getString("Country"));
        }
        return countryList;
    }

    /**
     * Gets an ObservableList of countries
     * @return countryList
     * @throws SQLException
     */
    public static ObservableList<String> getDivisions(String country) throws SQLException {
        ResultSet countryIdResults = JDBC.searchDB("Select Country_ID from countries where Country = \"" + country + "\"");
        countryIdResults.next();
        int countryId = countryIdResults.getInt("Country_ID");
        ResultSet divisions = JDBC.searchDB("Select Division from first_level_divisions where Country_ID = " + countryId);
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        while (divisions.next()) {
            divisionList.add(divisions.getString("Division"));
        }
        return divisionList;
    }

    /**
     * Gets contacts from DB
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> getContacts() throws SQLException {
        ObservableList<String> contacts = FXCollections.observableArrayList();
        ResultSet contactResults = JDBC.searchDB("Select Contact_Name from contacts");
        while (contactResults.next()) {
            contacts.add(contactResults.getString("Contact_Name"));
        }
        return contacts;
    }

    /**
     * Checks if two dates overlap
     * @param existingStart
     * @param existingEnd
     * @param utcStart
     * @param utcEnd
     * @return
     */
    public static boolean hasOverlap(ZonedDateTime existingStart, ZonedDateTime existingEnd, ZonedDateTime utcStart,
                                      ZonedDateTime utcEnd) {
        return existingStart.isBefore(utcEnd) && utcStart.isBefore(existingEnd);
    }

    /**
     * Gets the current UTC time
     * @return
     */
    public static LocalDateTime getCurrentUtcTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(formatter.format(Instant.now()), dateFormatter);
    }

    /**
     * Checks if a field is blank
     * @param field
     */
    public static void isBlank(TextField field) {
        if (field.getText().isEmpty()) {
            Common.throwError("Please fill in the " + field.getPromptText() + " field.");
        }
    }
}
