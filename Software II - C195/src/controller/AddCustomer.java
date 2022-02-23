package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Class to add customers to the database
 */
public class AddCustomer implements Initializable {
    public TextField AddCustName;
    public TextField AddCustAddress;
    public TextField AddCustPost;
    public TextField AddCustPhone;
    public Button AddCustomerSaveButton;
    public Button AddCustomerCancelButton;
    public ComboBox AddCustomerCountry;
    public ComboBox AddCustomerDivision;

    /**
     * Initializes the AddCustomer screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            AddCustomerCountry.setItems(Common.getCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        AddCustomerCountry.getSelectionModel().selectFirst();
        // Fake action event to get the division list to populate on initialization
        ActionEvent gooseDivision = new ActionEvent();
        try {
            CountrySelected(gooseDivision);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function for saving a customer
     * @param mouseEvent save button pressed
     * @throws IOException
     * @throws SQLException
     */
    public void AddCustomerSave(MouseEvent mouseEvent) throws IOException, SQLException {
        List<TextField> inputFields = Arrays.asList(AddCustName, AddCustAddress, AddCustPost, AddCustPhone);
//        for (TextField field : inputFields) {
//            if (field.getText().isEmpty()) {
//                Common.throwError("Please fill in the " + field.getPromptText() + " field.");
//                return;
//            }
//        }
        // Lambda function to check if fields are blank
        inputFields.forEach((n) -> Common.isBlank(n));
        ResultSet divisionIdResults = JDBC.searchDB("Select Division_ID from first_level_divisions where Division = \"" + AddCustomerDivision.getValue() + "\"");
        divisionIdResults.next();
        int division = divisionIdResults.getInt("Division_ID");
        JDBC.updateDB("Insert Into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                "values (\"" + AddCustName.getText() + "\", \"" + AddCustAddress.getText() + "\", \"" + AddCustPost.getText() + "\", \"" + AddCustPhone.getText() + "\", " + division + ")");
        Common.changeScene("CustomerRecords", AddCustomerSaveButton);
    }

    /**
     * Populates a division list when a country is selected
     * @param actionEvent
     * @throws SQLException
     */
    public void CountrySelected(ActionEvent actionEvent) throws SQLException {
        AddCustomerDivision.setItems(Common.getDivisions((String) AddCustomerCountry.getValue()));
        AddCustomerDivision.getSelectionModel().selectFirst();
    }

    /**
     * Returns to the customer records page
     * @param mouseEvent
     * @throws IOException
     */
    public void AddCustomerCancel(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("CustomerRecords", AddCustomerCancelButton);
    }
}
