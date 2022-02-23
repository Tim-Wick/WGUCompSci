package controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {
    public Label UpdateCustomerLabel;
    public TextField UpdateCustomerID;
    public TextField UpdateCustomerName;
    public TextField UpdateCustomerAddress;
    public TextField UpdateCustomerPost;
    public TextField UpdateCustomerPhone;
    public Button UpdateCustomerSaveButton;
    public Button UpdateCustomerCancelButton;
    public ComboBox UpdateCustomerCountry;
    public ComboBox UpdateCustomerDivision;

    /**
     * Initializes the updateCustomer screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Customer updateCustomer = CustomerRecords.getUpdateCustomer();
        String updateCustomerDivision = null;
        int updateCustomerCountryId;
        String updateCustomerCountry = null;
        try {
            ResultSet rs = JDBC.searchDB("Select Division, Country_ID from first_level_divisions Where Division_ID = " + updateCustomer.getDivision());
            rs.next();
            updateCustomerDivision = rs.getString("Division");
            updateCustomerCountryId = rs.getInt("Country_ID");
            ResultSet countryNameRs = JDBC.searchDB("Select Country from countries where Country_ID = " + updateCustomerCountryId);
            countryNameRs.next();
            updateCustomerCountry = countryNameRs.getString("Country");
            UpdateCustomerCountry.setItems(Common.getCountries());
            UpdateCustomerDivision.setItems(Common.getDivisions(updateCustomerCountry));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UpdateCustomerID.setText(String.valueOf(updateCustomer.getId()));
        UpdateCustomerName.setText(updateCustomer.getName());
        UpdateCustomerAddress.setText(updateCustomer.getAddress());
        UpdateCustomerPost.setText(updateCustomer.getPostCode());
        UpdateCustomerPhone.setText(updateCustomer.getPhone());
        UpdateCustomerDivision.setValue(updateCustomerDivision);
        UpdateCustomerCountry.setValue(updateCustomerCountry);

    }

    /**
     * Saves the changes to a Customer when save button is pressed
     * @param mouseEvent
     * @throws IOException
     * @throws SQLException
     */
    public void UpdateCustomerSave(MouseEvent mouseEvent) throws IOException, SQLException {
        List<TextField> inputFields = Arrays.asList(UpdateCustomerName, UpdateCustomerAddress, UpdateCustomerPost, UpdateCustomerPhone);
        for (TextField field : inputFields) {
            if (field.getText().isEmpty()) {
                Common.throwError("Please fill in the " + field.getPromptText() + " field.");
                return;
            }
        }
        ResultSet divisionIdResults = JDBC.searchDB("Select Division_ID from first_level_divisions where Division = \"" + UpdateCustomerDivision.getValue() + "\"");
        divisionIdResults.next();
        int division = divisionIdResults.getInt("Division_ID");
        JDBC.updateDB("Update customers SET Customer_Name = \"" + UpdateCustomerName.getText() + "\", Address = \"" +
                UpdateCustomerAddress.getText() + "\", Postal_Code = \"" + UpdateCustomerPost.getText() + "\", Phone = \"" +
                UpdateCustomerPhone.getText() + "\", Division_ID = " + division + " where Customer_Id = " + UpdateCustomerID.getText());
        Common.changeScene("CustomerRecords", UpdateCustomerSaveButton);
    }

    /**
     * Cancels and returns to the CustomerRecords screen
     * @param mouseEvent
     * @throws IOException
     */
    public void UpdateCustomerCancel(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("CustomerRecords", UpdateCustomerCancelButton);
    }

    /**
     * Updates list of divisions when country is selected
     * @param actionEvent
     * @throws SQLException
     */
    public void CountrySelectedUpdate(ActionEvent actionEvent) throws SQLException {
        UpdateCustomerDivision.setItems(Common.getDivisions((String) UpdateCustomerCountry.getValue()));
        UpdateCustomerDivision.getSelectionModel().selectFirst();
    }

}
