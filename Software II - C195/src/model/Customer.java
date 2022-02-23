package model;

/**
 * Class for creating and managing Customer objects
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postCode;
    private String phone;
    private int division;

    /**
     * Constructor for customer
     * @param id id value
     * @param name customer name
     * @param address customer address
     * @param postCode customer postal code
     * @param phone customer phone number
     * @param division customer division number
     */
    public Customer(int id, String name, String address, String postCode, String phone, int division) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Setters for Customer attributes
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    /**
     * Getters for Customer attributes
     */
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public int getDivision() {
        return this.division;
    }

}
