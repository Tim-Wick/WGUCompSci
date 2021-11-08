package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import main.Common;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for the modify product scene
 *
 * Logical error - min and max values were swapped when modifying a part.
 *
 * Future enhancement - Granular type checking for entered information into the form letting the user know exactly what
 * fields need different types.
 */
public class ModifyProduct implements Initializable {
    public TextField ModifyProductSearch;
    public TableView ModifyProductTopTable;
    public TableView ModifyProductBottomTable;
    public Button ModifyProductAdd;
    public Button ModifyProductCancel;
    public Button ModifyProductSave;
    public Button ModifyProductRemove;
    public TextField ModifyProductID;
    public TextField ModifyProductName;
    public TextField ModifyProductInventory;
    public TextField ModifyProductPrice;
    public TextField ModifyProductMax;
    public TextField ModifyProductMin;
    public TableColumn ModifyProdTopPartIDCol;
    public TableColumn ModifyProdTopNameCol;
    public TableColumn ModifyProdTopInvCol;
    public TableColumn ModifyProdBottomPartIDCol;
    public TableColumn ModifyProdBottomNameCol;
    public TableColumn ModifyProdBottomInvCol;
    public TableColumn ModifyProdBottomPriceCol;
    public TableColumn ModifyProdTopPriceCol;
    private Product selectedProduct = MainScreen.getModifiedProduct();

    /**
     * Initialzes the modify product scene
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initializing product screen
        // Get all parts
        ModifyProductTopTable.setItems(Inventory.getAllParts());
        ModifyProdTopPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProdTopNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProdTopInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProdTopPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        ModifyProdBottomPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProdBottomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProdBottomInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProdBottomPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        Product selectedProduct = MainScreen.getModifiedProduct();
        ModifyProductID.setText(String.valueOf(selectedProduct.getId()));
        ModifyProductName.setText(selectedProduct.getName());
        ModifyProductInventory.setText(String.valueOf(selectedProduct.getStock()));
        ModifyProductPrice.setText(String.valueOf(selectedProduct.getPrice()));
        ModifyProductMax.setText(String.valueOf(selectedProduct.getMax()));
        ModifyProductMin.setText(String.valueOf(selectedProduct.getMin()));
        ModifyProductBottomTable.setItems(selectedProduct.getAllAssociatedParts());
    }

    /**
     * Adds a part to the product parts list
     * @param mouseEvent
     */
    public void ModifyProductAdd(MouseEvent mouseEvent) {
        Part selectedPart = (Part) ModifyProductTopTable.getSelectionModel().getSelectedItem();
        if (!selectedProduct.getAllAssociatedParts().contains(selectedPart)) {
            selectedProduct.addAssociatedPart(selectedPart);
        }
        ModifyProductBottomTable.setItems(selectedProduct.getAllAssociatedParts());
    }

    /**
     * Cancels and returns to the main screne
     * @throws IOException
     */
    public void ModifyProductCancel() throws IOException {
        Common.changeScene("MainScreen", ModifyProductCancel);
    }

    /**
     * Saves a modified product to the inventory
     * @param mouseEvent
     */
    public void ModifyProductSave(MouseEvent mouseEvent) {
        try {
            String name = ModifyProductName.getText();
            int inventory = Integer.parseInt(ModifyProductInventory.getText());
            int price = Integer.parseInt(ModifyProductPrice.getText());
            int max = Integer.parseInt(ModifyProductMax.getText());
            int min = Integer.parseInt(ModifyProductMin.getText());
            if (!(min <= inventory) || !(inventory <= max || min >= max)) {
                Common.throwError("Please check min, max, and inventory.");
            } else {
                Inventory.updateProduct(selectedProduct, name, price, inventory, min, max);
                Common.changeScene("MainScreen", ModifyProductSave);
            }
        } catch (Exception e) {
            Common.throwError("Looks like some fields don't have the correct data types.");
        }
    }

    /**
     * Removes a part from the selected product
     * @param mouseEvent
     */
    public void ModifyProductRemove(MouseEvent mouseEvent) {
        Part selectedPart = (Part) ModifyProductBottomTable.getSelectionModel().getSelectedItem();
        if (Common.getConfirmation("Are you sure you want to remove " + selectedPart.getName() + "?")) {
            selectedProduct.deleteAssociatedPart(selectedPart);
        }
        ModifyProductBottomTable.setItems(selectedProduct.getAllAssociatedParts());
    }

    /**
     * Searches for parts in the inventory
     * @param keyEvent
     */
    public void ModifyProductSearchAction(KeyEvent keyEvent) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        String searchValue = ModifyProductSearch.getText();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(searchValue.toLowerCase()) || String.valueOf(part.getId()).equals(searchValue)) {
                returnedParts.add(part);
            }
        }
        if (returnedParts.isEmpty()) {
            Common.throwError("No part matches that search!");
        }
        ModifyProductTopTable.setItems(returnedParts);
    }

}
