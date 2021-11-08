package model;

/**
 * Class representing an In House part
 */
public class InHousePart extends Part{

    private int machineId;

    /**
     * Constructor for InHouse Part, takes below parameters
     *
     * @param id - id value of the part
     * @param name - name for the part
     * @param price - price for the part
     * @param stock - stock amount for the part
     * @param min - min amount for the part
     * @param max - max amount for the part
     * @param machineId - machine ID for the part
     */
    public InHousePart(int id, String name, int price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Setter for machine ID
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Getter for machine ID
     * @return
     */
    public int getMachineId() {
        return this.machineId;
    }

}
