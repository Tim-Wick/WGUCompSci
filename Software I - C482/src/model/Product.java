package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private int price;
    private int stock;
    private int min;
    private int max;

    /**
     *
     * @param id - id for the product
     * @param name - name for the product
     * @param price - price for the product
     * @param stock - stock amount for the product
     * @param min - min stock for the product
     * @param max - max stock for the product
     */
    public Product(int id, String name, int price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Setter for id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for price
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Setter for stock
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Setter for min
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Setter for max
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Getter for id
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Getter for string
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for price
     * @return
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Getter for stock
     * @return
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Getter for min
     * @return
     */
    public int getMin() {
        return this.min;
    }

    /**
     * Getter for max
     * @return
     */
    public int getMax() {
        return this.max;
    }

    /**
     * Adds a part to the associatedParts observable list
     * @param part
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Removes a part from the associatedParts observable list
     * @param selectedAssociatedPart
     * @return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    /**
     * Getter for the associatedParts observable list
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}
