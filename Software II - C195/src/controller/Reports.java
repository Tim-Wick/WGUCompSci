package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Reports implements Initializable {
    public ComboBox MonthPicker;
    public ComboBox TypePicker;
    public Label CombinedLabel;
    public TextField MonthResult;
    public TextField TypeResult;
    public TextField CombinedResult;
    public TableView ContactTable;
    public TableColumn ContactApptId;
    public TableColumn ContactTitle;
    public TableColumn ContactType;
    public TableColumn ContactStart;
    public TableColumn ContactEnd;
    public TableColumn ContactCustId;
    public TableColumn ContactDescription;
    public TableView CustomerTable;
    public TableColumn CustomerApptId;
    public TableColumn CustomerTitle;
    public TableColumn CustomerType;
    public TableColumn CustomerStart;
    public TableColumn CustomerEnd;
    public TableColumn CustomerCustId;
    public TableColumn CustomerDescription;
    public ComboBox ContactPicker;
    public ComboBox CustomerPicker;
    public Label MonthLabel;
    public Label TypeLabel;
    public Button ReportsBack;
    private int selectedMonthGlobal;
    private String selectedTypeGlobal;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate Months
        ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April",
                "May", "June", "July", "August", "September", "October", "November", "Decemter");
        MonthPicker.setItems(months);
        // Populate dropdown menus
        try {
            TypePicker.setItems(JDBC.getAllFields("Appointments", "Type"));
            ContactPicker.setItems(JDBC.getAllFields("Contacts", "Contact_Name"));
            CustomerPicker.setItems(JDBC.getAllFields("Customers", "Customer_Name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void MonthSelected(ActionEvent actionEvent) throws SQLException {
        String selectedMonth = (String) MonthPicker.getValue();
        MonthLabel.setText("Total for " + selectedMonth);
        int monthInt = 0;
        switch(selectedMonth) {
            case "January":
                monthInt = 1;
                break;
            case "February":
                monthInt = 2;
                break;
            case "March":
                monthInt = 3;
                break;
            case "April":
                monthInt = 4;
                break;
            case "May":
                monthInt = 5;
                break;
            case "June":
                monthInt = 6;
                break;
            case "July":
                monthInt = 7;
                break;
            case "August":
                monthInt = 8;
                break;
            case "September":
                monthInt = 9;
                break;
            case "October":
                monthInt = 10;
                break;
            case "November":
                monthInt = 11;
                break;
            case "December":
                monthInt = 12;
                break;
        }
        selectedMonthGlobal = monthInt;
        ResultSet monthCount = JDBC.searchDB("Select count(*) from appointments where year(start) = " +
                "year(curdate()) and month(start) = \"" + monthInt + "\"");
        monthCount.next();
        MonthResult.setText(monthCount.getString("count(*)"));
        MonthOrTypeSelected(new ActionEvent());
    }

    public void TypeSelected(ActionEvent actionEvent) throws SQLException {
        String selectedType = (String) TypePicker.getValue();
        selectedTypeGlobal = selectedType;
        TypeLabel.setText("Total for " + selectedType);
        ResultSet typeCount = JDBC.searchDB("Select count(*) from appointments where year(start) = year(curdate())" +
                "and type = \"" + selectedType + "\"");
        typeCount.next();
        TypeResult.setText(typeCount.getString("count(*)"));
        MonthOrTypeSelected(new ActionEvent());
    }

    public void MonthOrTypeSelected(ActionEvent actionEvent) throws SQLException {
        int selectedMonth = selectedMonthGlobal;
        String selectedType = selectedTypeGlobal;
        CombinedLabel.setText("Total for " + MonthPicker.getValue() + " and " + selectedType);
        ResultSet combinedCount = JDBC.searchDB("Select count(*) from appointments where year(start) = year(curdate()) " +
                "and month(start) = \"" + selectedMonth + "\" and type = \"" + selectedType + "\"");
        combinedCount.next();
        CombinedResult.setText(combinedCount.getString("count(*)"));
    }

    public void ContactSelected(ActionEvent actionEvent) throws SQLException {
        String selectedContact = (String) ContactPicker.getValue();
        ContactTable.setItems(JDBC.getAppointmentByField("contact", selectedContact));
        ContactApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ContactTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        ContactDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        ContactType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        ContactStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        ContactEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        ContactCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    public void CustomerSelected(ActionEvent actionEvent) throws SQLException {
        String selectedCustomer = (String) CustomerPicker.getValue();
        CustomerTable.setItems(JDBC.getAppointmentByField("customer", selectedCustomer));
        CustomerApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        CustomerTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        CustomerDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        CustomerType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        CustomerStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        CustomerEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        CustomerCustId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }

    public void ReportsGoBack(MouseEvent mouseEvent) throws IOException {
        Common.changeScene("MainScreen", ReportsBack);
    }

}
