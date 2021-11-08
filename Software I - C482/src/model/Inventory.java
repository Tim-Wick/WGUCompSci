package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for an inventory of Parts
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a new part to the inventory
     * @param newPart
     */
    public static void addPart(Part newPart) {
        if (!allParts.contains(newPart)) {
            allParts.add(newPart);
        } else {
          Common.throwError("Part already exists!");
        }
    }

    /**
     * Adds a new product to the inventory
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        if (!allProducts.contains(newProduct)) {
            allProducts.add(newProduct);
        } else {
            Common.throwError("Product already exists!");
        }
    }

    /**
     * Searches for a part by part ID and returns the part
     * @param partId - ID value for the part
     * @return
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        Common.throwError("Part with that ID does not exist in the inventory!");
        return null;
    }

    /**
     * Looks up a product based on product ID
     * @param productId
     * @return
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        Common.throwError("Product with that ID does not exist in the inventory!");
        return null;
    }

    /**
     * Looks up a part based on part name
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> returnPartsList = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                returnPartsList.add(part);
            }
        }
        if (!returnPartsList.isEmpty()) {
            return returnPartsList;
        } else {
            Common.throwError("Part with that name does not exist in the inventory!");
            return null;
        }
    }

    /**
     * Looks up a product based on product name
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> returnProductsList = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                returnProductsList.add(product);
            }
        }
        if (!returnProductsList.isEmpty()) {
            return returnProductsList;
        } else {
            Common.throwError("Product with that names does not exist in the inventory!");
            return null;
        }

    }

    /**
     * Updates the part with the given inputs
     * @param selectedPart
     */
    public static void updatePart(Part selectedPart, String name, int price, int stock, int min, int max) {
        selectedPart.setName(name);
        selectedPart.setPrice(price);
        selectedPart.setStock(stock);
        selectedPart.setMin(min);
        selectedPart.setMax(max);
    }

    /**
     * Updates the product at the given index in allProducts
     * @param selectedProduct
     */
    public static void updateProduct(Product selectedProduct, String name, int price, int stock, int min, int max) {
        selectedProduct.setName(name);
        selectedProduct.setPrice(price);
        selectedProduct.setStock(stock);
        selectedProduct.setMin(min);
        selectedProduct.setMax(max);
    }

    /**
     * Removes a part from allParts
     * No error checking needed here since parts will be selected from the GUI so only existing parts will be passed in
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * Removes a product from allProducts
     * Same applies as above for error checking
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * Getter for allParts
     * @return
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Getter for allProducts
     * @return
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public static int generateId(String type) {
        int newId = 0;
        List<Object> idList = new ArrayList<>();
        if (type.equals("part")) {
            for (Part part : Inventory.getAllParts()) {
                idList.add(part.getId());
            }
        } else {
            for (Product product : Inventory.getAllProducts()) {
                idList.add(product.getId());
            }
        }

        while (idList.contains(newId) || newId == 0) {
            Random random = new Random();
            newId = random.nextInt(1000);
        }
        return newId;

    }

}
