package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerRecords implements Initializable {
    public Button CustomerAddButton;
    public Button CustomerAppointmentButton;
    public Button CustomerDeleteButton;
    public Button CustomerHome;
    public Button CustomerUpdateButton;
    public TableView CustomerTable;
    public TableColumn CustomerRecordsIdCol;
    public TableColumn CustomerRecordsNameCol;
    public TableColumn CustomerRecordsAddressCol;
    public TableColumn CustomerRecordsPostCol;
    public TableColumn CustomerRecordsPhoneCol;
    public TableColumn CustomerRecordsDivCol;
    private static Customer updateCustomer;

    /**
     * Initializes the CustomerRecords screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateCustomerTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves to the AddCustomer screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToAddCustomer(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("AddCustomer", CustomerAddButton);
    }

    /**
     * Moves to the UpdateCustomer screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToUpdateCustomer(MouseEvent mouseEvent) throws IOException {
        updateCustomer = (Customer) CustomerTable.getSelectionModel().getSelectedItem();
        Common.changeScene("UpdateCustomer", CustomerUpdateButton);
    }

    /**
     * Moves to the Appointment screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoToAppointments(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("ScheduleCustomer", CustomerAppointmentButton);
    }

    /**
     * Deletes the customer
     * @param mouseEvent
     * @throws SQLException
     */
    public void DeleteCustomer(MouseEvent mouseEvent) throws SQLException {
        Customer selectedCustomer = (Customer) CustomerTable.getSelectionModel().getSelectedItem();
        if (Common.getConfirmation("Are you sure you want to delete customer " + selectedCustomer.getName() + "?")) {
            JDBC.updateDB("Delete from appointments where Customer_ID = " + selectedCustomer.getId());
            JDBC.updateDB("Delete from customers where Customer_ID = " + selectedCustomer.getId());
            Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
            deletedAlert.setContentText(selectedCustomer.getName() + " deleted.");
            populateCustomerTable();
            deletedAlert.showAndWait();
        }

    }

    /**
     * Goes to the home screen
     * @param mouseEvent
     * @throws IOException
     */
    public void GoHome(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("MainScreen", CustomerHome);
    }

    /**
     * Helper function to populate the customer table
     * @throws SQLException
     */
    private void populateCustomerTable() throws SQLException {
        CustomerTable.setItems(JDBC.getAllCustomers());
        CustomerRecordsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        CustomerRecordsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        CustomerRecordsAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        CustomerRecordsPostCol.setCellValueFactory(new PropertyValueFactory<>("postCode"));
        CustomerRecordsPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        CustomerRecordsDivCol.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    public static Customer getUpdateCustomer() {
        return updateCustomer;
    }
}
