package model;

/**
 * Class representing an Outsourced part
 */
public class OutsourcedPart extends Part{

    private String companyName;

    /**
     * Constructor for Outsourced Part, takes below parameters
     *
     * @param id - id value of the part
     * @param name - name for the part
     * @param price - price for the part
     * @param stock - stock amount for the part
     * @param min - min amount for the part
     * @param max - max amount for the part
     * @param companyName - company name for the part
     */
    public OutsourcedPart(int id, String name, int price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Setter for company name
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Getter for company name
     * @return
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
