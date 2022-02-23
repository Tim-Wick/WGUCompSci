package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {
    public Label UpdateAppointmentLabel;
    public TextField UpdateAppointmentID;
    public TextField UpdateAppointmentTitle;
    public TextField UpdateAppointmentDescription;
    public TextField UpdateAppointmentLocation;
    public Button UpdateAppointmentSaveButton;
    public Button UpdateAppointmentCancelButton;
    public TextField UpdateAppointmentType;
    public TextField UpdateAppointmentUserID;
    public TextField UpdateAppointmentCustID;
    public ComboBox UpdateAppointmentContact;
    public TextField UpdateStartTime;
    public TextField UpdateEndTime;
    public DatePicker UpdateStartDate;
    public DatePicker UpdateEndDate;

    /**
     * Initializes the update appointment screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Appointment updateAppointment = ScheduleCustomer.getUpdateAppointment();
        UpdateAppointmentID.setText(String.valueOf(updateAppointment.getId()));
        UpdateAppointmentTitle.setText(updateAppointment.getTitle());
        UpdateAppointmentDescription.setText(updateAppointment.getDescription());
        UpdateAppointmentLocation.setText(updateAppointment.getLocation());
        UpdateAppointmentType.setText(updateAppointment.getAppointmentType());
        UpdateAppointmentUserID.setText(String.valueOf(updateAppointment.getUserId()));
        UpdateAppointmentCustID.setText(String.valueOf(updateAppointment.getCustomerId()));
        try {
            UpdateAppointmentContact.setItems(Common.getContacts());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UpdateAppointmentContact.setValue(updateAppointment.getContactName());
        String start = updateAppointment.getStartTime();
        String end = updateAppointment.getEndTime();
        UpdateStartDate.setValue(LocalDate.parse(start.substring(0, 10)));
        UpdateStartTime.setText(start.substring(11, 16));
        UpdateEndDate.setValue(LocalDate.parse(end.substring(0, 10)));
        UpdateEndTime.setText(end.substring(11, 16));
    }

    /**
     * Saves an update appointment
     * @param mouseEvent
     * @throws IOException
     * @throws SQLException
     */
    public void UpdateAppointmentSave(MouseEvent mouseEvent) throws IOException, SQLException {
        List<TextField> inputFields = Arrays.asList(UpdateAppointmentTitle, UpdateAppointmentDescription, UpdateAppointmentLocation,
                UpdateAppointmentType, UpdateStartTime, UpdateEndTime, UpdateAppointmentCustID, UpdateAppointmentUserID);
        for (TextField field : inputFields) {
            if (field.getText().isEmpty()) {
                Common.throwError("Please fill in the " + field.getPromptText() + " field.");
                return;
            }
        }

        // Setting up time variables
        ZoneId localZone = ZoneId.systemDefault();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId utcZone = ZoneId.of("UTC");
        ZoneId estZone = ZoneId.of("America/New_York");
        LocalDateTime localStart;
        LocalDateTime localEnd;

        // Error checking for formatting
        try {
            localStart = LocalDateTime.parse(UpdateStartDate.getValue() + "T" + UpdateStartTime.getText());
            localEnd = LocalDateTime.parse(UpdateEndDate.getValue() + "T" + UpdateEndTime.getText());
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
        int customerId = Integer.parseInt(UpdateAppointmentCustID.getText());
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
        ResultSet contacts = JDBC.searchDB("Select Contact_ID from contacts where Contact_Name = \"" + UpdateAppointmentContact.getValue() + "\"");
        contacts.next();
        int contactId = contacts.getInt("Contact_ID");

        // Set up SQL timestamps
        Timestamp sqlStart = Timestamp.valueOf(utcStart.toLocalDateTime());
        Timestamp sqlEnd = Timestamp.valueOf(utcEnd.toLocalDateTime());

        // Update appointment
        JDBC.updateDB("Update appointments SET Title = \"" + UpdateAppointmentTitle.getText() + "\", Description = \"" +
                UpdateAppointmentDescription.getText() + "\", Location = \"" + UpdateAppointmentLocation.getText() + "\", " +
                "Type = \"" + UpdateAppointmentType.getText() + "\", Start = \"" + sqlStart + "\", End = \"" + sqlEnd + "\", Customer_ID = " +
                "" + customerId + ", User_ID = " + Integer.parseInt(UpdateAppointmentUserID.getText()) + ", Contact_ID = " + contactId + "" +
                " where Appointment_ID = " + UpdateAppointmentID.getText());

        Common.changeScene("ScheduleCustomer", UpdateAppointmentSaveButton);
    }

    /**
     * Cancles and returns to the main schedule screen
     * @param mouseEvent
     * @throws IOException
     */
    public void UpdateAppointmentCancel(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("ScheduleCustomer", UpdateAppointmentCancelButton);
    }
}
