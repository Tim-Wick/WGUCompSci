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
 * Class for the scene adding a product to the inventory
 * Logical error - Initially only inventory being between min and max was checked but min being greater than max
 * was allowed.
 *
 * Future Enhancement - Granular type checking for entered information into the form letting the user know exactly what
 * fields need different types.
 *  *
 */
public class AddProduct implements Initializable {
    public TextField AddProductSearch;
    public TableView AddProductTopTable;
    public TableView AddProductBottomTable;
    public Button AddProductAdd;
    public Button AddProductCancel;
    public Button AddProductSave;
    public Button AddProductRemove;
    public TextField AddProductID;
    public TextField AddProductName;
    public TextField AddProductInventory;
    public TextField AddProductPrice;
    public TextField AddProductMax;
    public TextField AddProductMin;
    public TableColumn AddProdTopPartIDCol;
    public TableColumn AddProdTopNameCol;
    public TableColumn AddProdTopInvCol;
    public TableColumn AddProdTopPriceCol;
    public TableColumn AddProdBottomPartIDCol;
    public TableColumn AddProdBottomNameCol;
    public TableColumn AddProdBottomInvCol;
    public TableColumn AddProdBottomPriceCol;
    private ObservableList<Part> newProductParts = FXCollections.observableArrayList();

    /**
     * Initializes the add product scene
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initializing product screen
        AddProductTopTable.setItems(Inventory.getAllParts());
        AddProdTopPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AddProdTopNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProdTopInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AddProdTopPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        AddProdBottomPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AddProdBottomNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProdBottomInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AddProdBottomPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Method for adding a selected part to the product
     * @param mouseEvent
     */
    public void AddProductAdd(MouseEvent mouseEvent) {
        Part selectedPart = (Part) AddProductTopTable.getSelectionModel().getSelectedItem();
        if (!newProductParts.contains(selectedPart)) {
            newProductParts.add(selectedPart);
        }
        AddProductBottomTable.setItems(newProductParts);
    }

    /**
     * Cancels and returns to the main screen
     * @throws IOException
     */
    public void AddProductCancel() throws IOException {
        Common.changeScene("MainScreen", AddProductCancel);
    }

    /**
     * Saves a new product to the inventory
     * @param mouseEvent
     */
    public void AddProductSave(MouseEvent mouseEvent) {
        try {
            String name = AddProductName.getText();
            int inventory = Integer.parseInt(AddProductInventory.getText());
            int price = Integer.parseInt(AddProductPrice.getText());
            int max = Integer.parseInt(AddProductMax.getText());
            int min = Integer.parseInt(AddProductMin.getText());
            if (!(min <= inventory) || !(inventory <= max || min >= max)) {
                Common.throwError("Please check min, max, and inventory.");
            } else {
                Product newProduct = new Product(Inventory.generateId("product"), name, price, inventory, min, max);
                for (Part part : newProductParts) {
                    newProduct.addAssociatedPart(part);
                }
                Inventory.addProduct(newProduct);
                Common.changeScene("MainScreen", AddProductSave);
            }
        } catch (Exception e) {
            Common.throwError("Looks like some fields don't have the correct data types.");
        }
    }

    /**
     * Removes a part from the product list
     * @param mouseEvent
     */
    public void AddProductRemove(MouseEvent mouseEvent) {
        Part selectedPart = (Part) AddProductBottomTable.getSelectionModel().getSelectedItem();
        newProductParts.remove(selectedPart);
        AddProductBottomTable.setItems(newProductParts);
    }

    /**
     * Searches parts in inventory
     * @param keyEvent
     */
    public void AddProductSearchAction(KeyEvent keyEvent) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        String searchValue = AddProductSearch.getText();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(searchValue.toLowerCase()) || String.valueOf(part.getId()).equals(searchValue)) {
                returnedParts.add(part);
            }
        }
        if (returnedParts.isEmpty()) {
            Common.throwError("No part matches that search!");
        }
        AddProductTopTable.setItems(returnedParts);
    }
}
