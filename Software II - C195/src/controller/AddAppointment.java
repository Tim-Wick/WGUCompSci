package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {
    public TextField AddAppointmentTitle;
    public TextField AddAppointmentDescription;
    public TextField AddAppointmentLocation;
    public Button AddAppointmentSaveButton;
    public Button AddAppointmentCancelButton;
    public TextField AddAppointmentType;
    public TextField AddAppointmentUserID;
    public TextField AddAppointmentCustID;
    public ComboBox AddAppointmentContact;
    public DatePicker AddStartDate;
    public TextField AddStartTime;
    public DatePicker AddEndDate;
    public TextField AddEndTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            AddAppointmentContact.setItems(Common.getContacts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddAppointmentContact.getSelectionModel().selectFirst();
    }

    public void AddAppointmentSave(MouseEvent mouseEvent) throws IOException, SQLException {
        List<TextField> inputFields = Arrays.asList(AddAppointmentTitle, AddAppointmentDescription, AddAppointmentLocation,
                AddAppointmentType, AddStartTime, AddEndTime, AddAppointmentCustID, AddAppointmentUserID);
//        for (TextField field : inputFields) {
//            if (field.getText().isEmpty()) {
//                Common.throwError("Please fill in the " + field.getPromptText() + " field.");
//                return;
//            }
//        }

        // Lambda function to check if fields are blank
        inputFields.forEach((n) -> Common.isBlank(n));

        // Setting up time variables
        ZoneId localZone = ZoneId.systemDefault();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId estZone = ZoneId.of("America/New_York");
        LocalDateTime localStart;
        LocalDateTime localEnd;

        // Error checking for formatting
        try {
            localStart = LocalDateTime.parse(AddStartDate.getValue() + "T" + AddStartTime.getText());
            localEnd = LocalDateTime.parse(AddEndDate.getValue() + "T" + AddEndTime.getText());
        } catch (DateTimeParseException e) {
            Common.throwError("Looks like a time is incorrectly formatted, please use HH:MM format in 24 hour time.");
            return;
        }

        // Error checking for timeline
        if (localStart.isAfter(localEnd)) {
            Common.throwError("Start time is after end time, please fix.");
            return;
        }

        // Error checking for business hours
        ZonedDateTime estStart = localStart.atZone(localZone).withZoneSameInstant(estZone);
        ZonedDateTime estEnd = localEnd.atZone(localZone).withZoneSameInstant(estZone);
        int estStartTime = estStart.getHour();
        int estEndTime = estEnd.getHour();
        if (estStartTime < 8 || estEndTime > 22) {
            Common.throwError("Please schedule appointments between 8:00 and 22:00 EST.");
        }

        // Setting up UTC times to insert into DB
        ZonedDateTime utcStart = localStart.atZone(localZone).withZoneSameInstant(utcZone);
        ZonedDateTime utcEnd = localEnd.atZone(localZone).withZoneSameInstant(utcZone);

        // Checks for overlapping appointments
        int customerId = Integer.parseInt(AddAppointmentCustID.getText());
        ResultSet customerAppointments = JDBC.searchDB("Select Start, End from Appointments where Customer_ID = " + customerId);
        while (customerAppointments.next()) {
            LocalDateTime existingStart = LocalDateTime.parse(customerAppointments.getString("Start"), dateFormatter);
            LocalDateTime existingEnd = LocalDateTime.parse(customerAppointments.getString("End"), dateFormatter);
            ZonedDateTime existingStartUtc = existingStart.atZone(utcZone);
            ZonedDateTime existingEndUtc = existingEnd.atZone(utcZone);
            if (Common.hasOverlap(existingStartUtc, existingEndUtc, utcStart, utcEnd)) {
                Common.throwError("Times overlap with another appointment, please choose another time.");
                return;
            }
        }

        // Get contact ID
        ResultSet contacts = JDBC.searchDB("Select Contact_ID from contacts where Contact_Name = \"" + AddAppointmentContact.getValue() + "\"");
        contacts.next();
        int contactId = contacts.getInt("Contact_ID");

        // Set up SQL timestamps
        Timestamp sqlStart = Timestamp.valueOf(utcStart.toLocalDateTime());
        Timestamp sqlEnd = Timestamp.valueOf(utcEnd.toLocalDateTime());

        // Insert appointment
        JDBC.updateDB("Insert into Appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "values (\"" + AddAppointmentTitle.getText() + "\", \"" + AddAppointmentDescription.getText() + "\", \"" + AddAppointmentLocation.getText() + "\", \""
        + AddAppointmentType.getText() + "\", \"" + sqlStart + "\", \"" + sqlEnd + "\", " + customerId + ", " + Integer.parseInt(AddAppointmentUserID.getText())
        + ", " + contactId + ")");

        Common.changeScene("ScheduleCustomer", AddAppointmentSaveButton);
    }

    public void AddAppointmentCancel(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("ScheduleCustomer", AddAppointmentCancelButton);
    }

}

interface ChronologicalOrder {
    public boolean inOrder(Appointment p);
}
