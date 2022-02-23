package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.SimpleFormatter;

public class ScheduleCustomer implements Initializable {
    public Label ScheduleLabel;
    public Button AppointmentAddButton;
    public Button AppointmentUpdateButton;
    public Button AppointmentDeleteButton;
    public Button ScheduleHome;
    public Button ScheduleBack;
    public RadioButton ScheduleTimeFrameMonth;
    public ToggleGroup ScheduleTimeFrame;
    public RadioButton ScheduleTimeFrameWeek;
    public TableView AppointmentTable;
    public TableColumn AppointmentId;
    public TableColumn AppointmentTitle;
    public TableColumn AppointmentDescription;
    public TableColumn AppointmentLocation;
    public TableColumn AppointmentContact;
    public TableColumn AppointmentType;
    @FXML public TableColumn<Appointment, String> AppointmentStart;
    public TableColumn AppointmentEnd;
    public TableColumn AppointmentCustomerId;
    public TableColumn AppointmentUserId;
    static Appointment updateAppointment;

    /**
     * Initializes the customer scheduling screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getTimeframeAppointments();
            populateAppointmentTable();
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Goes to the addAppointment screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToAddAppointment(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("AddAppointment", AppointmentAddButton);
    }

    /**
     * Goes to the updateAppointment screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToUpdateAppointment(MouseEvent mouseEvent) throws IOException {
        updateAppointment = (Appointment) AppointmentTable.getSelectionModel().getSelectedItem();
        Common.changeScene("UpdateAppointment", AppointmentUpdateButton);
    }

    /**
     * Deletes an appointment
     * @param mouseEvent
     * @throws SQLException
     * @throws ParseException
     */
    public void DeleteAppointment(MouseEvent mouseEvent) throws SQLException, ParseException {
        Appointment selectedAppointment = (Appointment) AppointmentTable.getSelectionModel().getSelectedItem();
        if (Common.getConfirmation("Are you sure you want to delete appointment " + selectedAppointment.getTitle() + "?")) {
            JDBC.updateDB("Delete from appointments where Appointment_ID = " + selectedAppointment.getId());
            Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
            deletedAlert.setContentText("Appointment ID " + selectedAppointment.getId() + " with type " + selectedAppointment.getAppointmentType() + " deleted.");
            getTimeframeAppointments();
            populateAppointmentTable();
            deletedAlert.showAndWait();
        }
    }

    /**
     * Goes to homepage
     * @param mouseEvent
     * @throws IOException
     */
    public void GoHome(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("MainScreen", ScheduleHome);
    }

    /**
     * Goes to customer page
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToCustomer(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("CustomerRecords", ScheduleBack);
    }

    /**
     * Filters appointments by week
     * @param mouseEvent
     * @throws SQLException
     * @throws ParseException
     */
    public void FilterTimeFrameWeek(MouseEvent mouseEvent) throws SQLException, ParseException {
        getTimeframeAppointments();
        populateAppointmentTable();
    }

    /**
     * Filters appointment by month
     * @param mouseEvent
     * @throws SQLException
     * @throws ParseException
     */
    public void FilterTimeFrameMonth(MouseEvent mouseEvent) throws SQLException, ParseException {
        getTimeframeAppointments();
        populateAppointmentTable();
    }

    /**
     * Returns the selected appointment to update
     * @return
     */
    public static Appointment getUpdateAppointment() {
        return updateAppointment;
    }

    /**
     * Populates the appointment table
     * @throws SQLException
     */
    @FXML
    private void populateAppointmentTable() throws SQLException {
        AppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        AppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        AppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        AppointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        AppointmentContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        AppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        AppointmentStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        AppointmentEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        AppointmentCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        AppointmentUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    /**
     * Gets the appointments inh the selected timeframe
     * @throws ParseException
     * @throws SQLException
     */
    private void getTimeframeAppointments() throws ParseException, SQLException {
        LocalDateTime utcTime = Common.getCurrentUtcTime();
        if (ScheduleTimeFrameWeek.isSelected()) {
            AppointmentTable.setItems(JDBC.getAppointmentTimeframe(utcTime, 7));
        } else {
            AppointmentTable.setItems(JDBC.getAppointmentTimeframe(utcTime, 30));
        }

    }

}
