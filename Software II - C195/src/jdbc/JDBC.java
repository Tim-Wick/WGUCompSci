package jdbc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    private static Connection connection = null;  // Connection Interface
    private static PreparedStatement preparedStatement;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId localZone = ZoneId.systemDefault();
    private static final ZoneId utcZone = ZoneId.of("UTC");

    public static void makeConnection() {
        try {
          Class.forName(driver); // Locate Driver
          //password = Details.getPassword(); // Assign password
          connection = DriverManager.getConnection(jdbcUrl, userName, password); // reference Connection object
          System.out.println("Connection successful!");
        }
          catch(ClassNotFoundException | SQLException e) {
              System.out.println("Error:" + e.getMessage());
          }
    }

    public static Connection getConnection() {
        return connection;
    }

     public static void closeConnection() {
         try {
             connection.close();
             System.out.println("Connection closed!");
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     }

    public static void makePreparedStatement(String sqlStatement, Connection conn) throws SQLException {
       if (conn != null)
           preparedStatement = conn.prepareStatement(sqlStatement);
       else
           System.out.println("Prepared Statement Creation Failed!");
    }

    public static PreparedStatement getPreparedStatement() throws SQLException {
       if (preparedStatement != null)
           return preparedStatement;
       else System.out.println("Null reference to Prepared Statement");
       return null;
    }

    /**
     * Searches database with given SQL query
     * @param searchString sql query
     * @return the ResultSet from the query
     * @throws SQLException
     */
    public static ResultSet searchDB(String searchString) throws SQLException {
        return connection.createStatement().executeQuery(searchString);
    }

    /**
     * Updates database with the given SQL query
     * @param createString sql query
     * @throws SQLException
     */
    public static void updateDB(String createString) throws SQLException {
        connection.createStatement().executeUpdate(createString);
    }

    /**
     * Method to get all customers from the database
     * @return ObservableList of Customer objects
     * @throws SQLException exception
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        ResultSet allCustomers = searchDB("select * from customers");
        while (allCustomers.next()) {
            customerList.add(new Customer(allCustomers.getInt("Customer_ID"),
                    allCustomers.getString("Customer_Name"), allCustomers.getString("Address"),
                    allCustomers.getString("Postal_Code"), allCustomers.getString("Phone"),
                    allCustomers.getInt("Division_ID")));
        }
        return customerList;
    }

    /**
     * Method to get all appointments from the database
     * @return Observable list of Appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ResultSet allAppointments = searchDB("select * from appointments");
        while (allAppointments.next()) {
            int contactId = allAppointments.getInt("Contact_ID");
            ResultSet contactNameRs = JDBC.searchDB("Select Contact_Name from contacts where Contact_ID = " + contactId);
            contactNameRs.next();
            String contactName = contactNameRs.getString("Contact_Name");
            LocalDateTime utcStart = LocalDateTime.parse(allAppointments.getString("Start"), dateFormatter);
            LocalDateTime utcEnd = LocalDateTime.parse(allAppointments.getString("End"), dateFormatter);
            ZonedDateTime localStart = utcStart.atZone(utcZone).withZoneSameInstant(localZone);
            ZonedDateTime localEnd = utcEnd.atZone(utcZone).withZoneSameInstant(localZone);
            String localStartString = localStart.format(dateFormatter);
            String localEndString = localEnd.format(dateFormatter);
            appointmentList.add(new Appointment(allAppointments.getInt("Appointment_ID"), allAppointments.getString("Title"),
                    allAppointments.getString("Description"), allAppointments.getString("Location"),
                    allAppointments.getString("Type"), localStartString, localEndString, allAppointments.getInt("Customer_ID"),
                    allAppointments.getInt("User_ID"), contactId, contactName));
        }
        return appointmentList;
    }

    /**
     * Method to get appointments in a given time frame
     * @param currentTime current date/time in UTC
     * @param timeframe timeframe to accept
     * @return Observable list of appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentTimeframe(LocalDateTime currentTime, int timeframe) throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ResultSet allAppointments = searchDB("select * from appointments");
        while (allAppointments.next()) {
            int contactId = allAppointments.getInt("Contact_ID");
            ResultSet contactNameRs = JDBC.searchDB("Select Contact_Name from contacts where Contact_ID = " + contactId);
            contactNameRs.next();
            String contactName = contactNameRs.getString("Contact_Name");
            LocalDateTime utcStart = LocalDateTime.parse(allAppointments.getString("Start"), dateFormatter);
            LocalDateTime utcEnd = LocalDateTime.parse(allAppointments.getString("End"), dateFormatter);
            if (utcStart.isAfter(currentTime) && utcStart.isBefore(currentTime.plusDays(timeframe))) {
                ZonedDateTime localStart = utcStart.atZone(utcZone).withZoneSameInstant(localZone);
                ZonedDateTime localEnd = utcEnd.atZone(utcZone).withZoneSameInstant(localZone);
                String localStartString = localStart.format(dateFormatter);
                String localEndString = localEnd.format(dateFormatter);
                appointmentList.add(new Appointment(allAppointments.getInt("Appointment_ID"), allAppointments.getString("Title"),
                        allAppointments.getString("Description"), allAppointments.getString("Location"),
                        allAppointments.getString("Type"), localStartString, localEndString, allAppointments.getInt("Customer_ID"),
                        allAppointments.getInt("User_ID"), contactId, contactName));
            }
        }
        return appointmentList;
    }

    public static ObservableList<String> getAllFields(String table, String field) throws SQLException {
        ObservableList<String> allFields = FXCollections.observableArrayList();
        ResultSet allResults = searchDB("Select Distinct " + field + " from " + table);
        while (allResults.next()) {
            allFields.add(allResults.getString(field));
        }
        return allFields;
    }

    public static ObservableList<Appointment> getAppointmentByField(String field, String selection) throws SQLException {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        ResultSet fieldIdRs = JDBC.searchDB("Select " + field + "_id from " + field + "s where " + field + "_name = \"" + selection + "\"");
        fieldIdRs.next();
        int fieldId = fieldIdRs.getInt(field + "_id");
        ResultSet selectedAppointments = searchDB("select * from appointments where " + field + "_id = " + fieldId);
        while (selectedAppointments.next()) {
            LocalDateTime utcStart = LocalDateTime.parse(selectedAppointments.getString("Start"), dateFormatter);
            LocalDateTime utcEnd = LocalDateTime.parse(selectedAppointments.getString("End"), dateFormatter);
            ZonedDateTime localStart = utcStart.atZone(utcZone).withZoneSameInstant(localZone);
            ZonedDateTime localEnd = utcEnd.atZone(utcZone).withZoneSameInstant(localZone);
            String localStartString = localStart.format(dateFormatter);
            String localEndString = localEnd.format(dateFormatter);
            appointmentList.add(new Appointment(selectedAppointments.getInt("Appointment_ID"), selectedAppointments.getString("Title"),
                    selectedAppointments.getString("Description"), selectedAppointments.getString("Location"),
                    selectedAppointments.getString("Type"), localStartString, localEndString, selectedAppointments.getInt("Customer_ID"),
                    selectedAppointments.getInt("User_ID"), selectedAppointments.getInt("Contact_ID"), "Null"));
            }
        return appointmentList;
    }

}