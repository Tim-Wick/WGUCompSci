package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import main.Common;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for the main screen of the inventory program
 *
 * Runtime error - selected part was not cast to (Part) and therefore deletion did not work. Casting correctly to (Part)
 * resolved the issue.
 *
 * Future enhancement - have option to remove related parts when trying to delete a product with related parts
 */
public class MainScreen implements Initializable {

    public AnchorPane MainScreenAnchorPane;
    public Button MainScreenExit;
    public Pane MainScreenPartsPane;
    public TextField MainScreenPartsSearch;
    public TableView MainScreenPartsTable;
    public Button MainScreenPartsAdd;
    public Button MainScreenPartsModify;
    public Button MainScreenPartsDelete;
    public Pane MainScreenProductsPane;
    public TextField MainScreenProductsSearch;
    public TableView MainScreenProductsTable;
    public Button MainScreenProductsAdd;
    public Button MainScreenProductsModify;
    public Button MainScreenProductsDelete;
    public TableColumn MainScreenProdIDCol;
    public TableColumn MainScreenProdNameCol;
    public TableColumn MainScreenProdInvCol;
    public TableColumn MainScreenProdPriceCol;
    public TableColumn MainScreenPartIDCol;
    public TableColumn MainScreenPartNameCol;
    public TableColumn MainScreenPartInvCol;
    public TableColumn MainScreenPartPriceCol;
    private static Part modifiedPart;
    private static Product modifiedProduct;

    /**
     * Initializes the main screen with part and product lists
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initializing main screen
        MainScreenPartsTable.setItems(Inventory.getAllParts());
        MainScreenPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainScreenPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainScreenPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainScreenPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        MainScreenProductsTable.setItems(Inventory.getAllProducts());
        MainScreenProdIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainScreenProdNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainScreenProdInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainScreenProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * Exits the program
     * @param mouseEvent
     */
    public void exit(MouseEvent mouseEvent) {
        Platform.exit();
    }

    /**
     * Goes to the add part scene
     * @throws IOException
     */
    public void addPart() throws IOException {
        Common.changeScene("AddPart", MainScreenPartsAdd);
    }

    /**
     * Goes to the modify part scene
     * @throws IOException
     */
    public void modifyPart() throws IOException {
        modifiedPart = (Part) MainScreenPartsTable.getSelectionModel().getSelectedItem();
        Common.changeScene("ModifyPart", MainScreenPartsModify);
    }

    /**
     * Deletes a part from the inventory
     * @param mouseEvent
     */
    public void deletePart(MouseEvent mouseEvent) {
        Part selectedPart = (Part) MainScreenPartsTable.getSelectionModel().getSelectedItem();
        if (Common.getConfirmation("Do you want to delete " + selectedPart.getName() + "?")) {
            Inventory.deletePart(selectedPart);
        }
    }

    /**
     * Goes to the add product scene
     * @throws IOException
     */
    public void addProduct() throws IOException {
        Common.changeScene("AddProduct", MainScreenProductsAdd);
    }

    /**
     * Goes to the modify product scene
     * @throws IOException
     */
    public void modifyProduct() throws IOException {
        modifiedProduct = (Product) MainScreenProductsTable.getSelectionModel().getSelectedItem();
        Common.changeScene("ModifyProduct", MainScreenProductsModify);
    }

    /**
     * Deletes a product from the inventory
     * @param mouseEvent
     */
    public void deleteProduct(MouseEvent mouseEvent) {
        Product selectedProduct = (Product) MainScreenProductsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            if (Common.getConfirmation("Do you want to delete " + selectedProduct.getName() + "?")) {
                Inventory.deleteProduct(selectedProduct);
            }
        } else {
            Common.throwError("There are parts associated with " + selectedProduct.getName() + ", please" +
                    "remove all related parts first.");
        }
    }

    /**
     * Searches for parts in the inventory
     * @param actionEvent
     */
    public void MainScreenPartsSearchAction(KeyEvent actionEvent) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();
        String searchValue = MainScreenPartsSearch.getText();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(searchValue.toLowerCase()) || String.valueOf(part.getId()).equals(searchValue)) {
                returnedParts.add(part);
            }
        }
        if (returnedParts.isEmpty()) {
            Common.throwError("No part matches that search!");
        }
        MainScreenPartsTable.setItems(returnedParts);

    }

    /**
     * Searches for products in the inventory
     * @param actionEvent
     */
    public void MainScreenProductSearchAction(KeyEvent actionEvent) {
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        String searchValue = MainScreenProductsSearch.getText();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(searchValue.toLowerCase()) || String.valueOf(product.getId()).equals(searchValue)) {
                returnedProducts.add(product);
            }
        }
        if (returnedProducts.isEmpty()) {
            Common.throwError("No product matches that search!");
        }
        MainScreenProductsTable.setItems(returnedProducts);

    }

    public static Part getModifiedPart() {
        return modifiedPart;
    }

    public static Product getModifiedProduct() { return modifiedProduct;}

}
