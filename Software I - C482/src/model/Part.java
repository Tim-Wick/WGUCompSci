package model;

/**
 * Class for Part
 */

public abstract class Part {
    private int id;
    private String name;
    private int price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor for Part, takes below parameters
     * @param id - id value of the part
     * @param name - name for the part
     * @param price - price for the part
     * @param stock - stock amount for the part
     * @param min - min amount for the part
     * @param max - max amount for the part
     */
    public Part(int id, String name, int price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Setter for id value
     * @param id
     */
    public void setID(int id) {
        this.id = id;
    }

    /**
     * Setter for name value
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for price value
     * @param price
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Setter for stock value
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Setter for min value
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Setter for max value
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
     * Getter for name
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

}
