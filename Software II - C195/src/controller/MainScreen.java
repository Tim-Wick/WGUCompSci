package controller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Appointment;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {
    public Button MainScreenRecords;
    public Button MainScreenReports;
    public Button MainScreenLogout;
    public Label MainScreenLabel;
    public Button MainScreenAppointments;

    /**
     * Initializes the MainScreen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String currentUser = LoginScreen.getCurrentUser();
        MainScreenLabel.setText("Welcome " + currentUser);
        try {
            // Pull out user ID
            ResultSet userResults = JDBC.searchDB("Select User_ID from Users where User_Name = \"" + currentUser + "\"");
            userResults.next();
            int userId = userResults.getInt("User_ID");

            // Get current user time
            ZoneId localZone = ZoneId.systemDefault();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZoneId utcZone = ZoneId.of("UTC");
            ZonedDateTime currentLocalTime = ZonedDateTime.now();

            // Get upcoming appointments for user
            ResultSet upcomingAppointments = JDBC.searchDB("Select * from Appointments where User_ID = " + userId);

            // Set up arraylist to keep track of users
            ArrayList<String> upcomingAppointmentsArr = new ArrayList<>();

            // Populate list with appointments
            while (upcomingAppointments.next()) {
                // Get ZoneDateTime of the appointment
                LocalDateTime utcStart = LocalDateTime.parse(upcomingAppointments.getString("Start"), dateFormatter);
                ZonedDateTime utcStartZoned = utcStart.atZone(utcZone);
                // Convert to local zone
                ZonedDateTime localStartZoned = utcStartZoned.withZoneSameInstant(localZone);
                if (localStartZoned.isAfter(currentLocalTime) && localStartZoned.isBefore(currentLocalTime.plusMinutes(15))) {
                    upcomingAppointmentsArr.add("Appointment ID; " + upcomingAppointments.getString("Appointment_ID") +
                            " at " + localStartZoned + ".");
                }
            }

            // Alert user accordingly
            if (!upcomingAppointmentsArr.isEmpty()) {
                Common.getConfirmation("Upcoming appointments within 15 minutes:\n" + upcomingAppointmentsArr.toString());
            } else {
                Common.getConfirmation("No upcoming appointments within 15 minutes");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Goes to the customer records screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToRecords(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("CustomerRecords", MainScreenRecords);

    }

    /**
     * Goes to the reports screen
     * @param mouseEvent
     */
    public void GoToReports(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("Reports", MainScreenReports);
    }

    public void Logout(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("LoginScreen", MainScreenLogout);

    }

    /**
     * Goes to the appointments screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToAppointments(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("ScheduleCustomer", MainScreenAppointments);
    }
}
