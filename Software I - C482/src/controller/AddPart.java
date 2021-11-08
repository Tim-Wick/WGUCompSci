package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Common;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for the scene adding a part to the inventory
 * Logical error - Initially tried to pass machineID straight from the form into InHousePart but needed to first
 * cast it to an integer.
 *
 * Future Enhancement - Granular type checking for entered information into the form letting the user know exactly what
 * fields need different types.
 *
 */
public class AddPart implements Initializable {
    public AnchorPane AddPartAnchorPane;
    public RadioButton AddPartInHouseRadio;
    public RadioButton AddPartOutsourcedRadio;
    public TextField AddPartPrice;
    public TextField AddPartMax;
    public TextField AddPartMachineID;
    public TextField AddPartInventory;
    public TextField AddPartName;
    public TextField AddPartID;
    public TextField AddPartMin;
    public Button AddPartSave;
    public Button AddPartCancel;
    public Label AddPartMachineIDLabel;

    /**
     * Initializes the AddPart screen
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Changes label based on in-house or outsourced part selection
     * @param mouseEvent
     */
    public void AddPartInHouse(MouseEvent mouseEvent) {
        AddPartMachineIDLabel.setText("Machine ID");
    }

    /**
     * Changes label based on in-house or outsourced part selection
     * @param mouseEvent
     */
    public void AddPartOutsourced(MouseEvent mouseEvent) {
        AddPartMachineIDLabel.setText("Company Name");
    }

    /**
     * Saves a new part to the inventory
     * @param mouseEvent
     * @throws IOException
     */
    public void AddPartSave(MouseEvent mouseEvent) throws IOException {
        try {
            String name = AddPartName.getText();
            int inventory = Integer.parseInt(AddPartInventory.getText());
            int price = Integer.parseInt(AddPartPrice.getText());
            int max = Integer.parseInt(AddPartMax.getText());
            int min = Integer.parseInt(AddPartMin.getText());
            String machineId = AddPartMachineID.getText();
            if (!(min <= inventory) || !(inventory <= max || min >= max)) {
                Common.throwError("Please check min, max, and inventory.");
            } else {
                if (AddPartInHouseRadio.isSelected()) {
                    InHousePart newPart = new InHousePart(Inventory.generateId("part"), name, price, inventory, min, max, Integer.parseInt(machineId));
                    Inventory.addPart(newPart);
                } else {
                    OutsourcedPart newPart = new OutsourcedPart(Inventory.generateId("part"), name, price, inventory, min, max, machineId);
                    Inventory.addPart(newPart);
                }
                Common.changeScene("MainScreen", AddPartSave);
            }
        } catch (Exception e) {
            Common.throwError("Looks like some fields don't have the correct data types.");
        }

    }

    /**
     * Cancels adding a new part and returns to the main screen
     * @throws IOException
     */
    public void AddPartCancel() throws IOException {
        Common.changeScene("MainScreen", AddPartCancel);
    }
}
