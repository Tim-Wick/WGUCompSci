package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.Common;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for the modify parts scene
 *
 * Logical error - min and max values were swapped when modifying a part.
 *
 * Future enhancement - allow modification of inhousepart to outsourcedpart and vice versa
 */
public class ModifyPart implements Initializable {
    public AnchorPane ModifyPartAnchorPane;
    public RadioButton ModifyPartInHouseRadio;
    public ToggleGroup ModifyPartToggleGroup;
    public RadioButton ModifyPartOutsourcedRadio;
    public TextField ModifyPartPrice;
    public TextField ModifyPartMax;
    public TextField ModifyPartMachineID;
    public TextField ModifyPartInventory;
    public TextField ModifyPartName;
    public TextField ModifyPartID;
    public TextField ModifyPartMin;
    public Button ModifyPartSave;
    public Button ModifyPartCancel;
    public Label ModifyPartMachineIDLabel;

    /**
     * Initializes the modify parts scene
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part selectedPart = MainScreen.getModifiedPart();
        if (selectedPart instanceof InHousePart) {
            ModifyPartInHouseRadio.setSelected(true);
            ModifyPartMachineID.setText(String.valueOf(((InHousePart) selectedPart).getMachineId()));
        } else {
            ModifyPartOutsourcedRadio.setSelected(true);
            ModifyPartMachineIDLabel.setText("Company Name");
            ModifyPartMachineID.setText(((OutsourcedPart) selectedPart).getCompanyName());
        }

        ModifyPartID.setText(String.valueOf(selectedPart.getId()));
        ModifyPartName.setText(selectedPart.getName());
        ModifyPartInventory.setText(String.valueOf(selectedPart.getStock()));
        ModifyPartPrice.setText(String.valueOf(selectedPart.getPrice()));
        ModifyPartMax.setText(String.valueOf(selectedPart.getMax()));
        ModifyPartMin.setText(String.valueOf(selectedPart.getMin()));

    }

    /**
     * Changes label based on in-house or outsourced part
     * @param mouseEvent
     */
    public void ModifyPartInHouse(MouseEvent mouseEvent) {
        ModifyPartMachineIDLabel.setText("Machine ID");
    }

    /**
     * Changes label based on in-house or outsourced part
     * @param mouseEvent
     */
    public void ModifyPartOutsourced(MouseEvent mouseEvent) {
        ModifyPartMachineIDLabel.setText("Company Name");
    }

    /**
     * Saves a modified part to the inventory
     * @param mouseEvent
     * @throws IOException
     */
    public void ModifyPartSave(MouseEvent mouseEvent) throws IOException {
        Part selectedPart = MainScreen.getModifiedPart();
        try {
            String name = ModifyPartName.getText();
            int inventory = Integer.parseInt(ModifyPartInventory.getText());
            int price = Integer.parseInt(ModifyPartPrice.getText());
            int max = Integer.parseInt(ModifyPartMax.getText());
            int min = Integer.parseInt(ModifyPartMin.getText());
            String machineId = ModifyPartMachineID.getText();
            if (!(min <= inventory) || !(inventory <= max || min >= max)) {
                Common.throwError("Please check min, max, and inventory.");
            } else {
                if (ModifyPartInHouseRadio.isSelected()) {
                    InHousePart newPart = new InHousePart(selectedPart.getId(), name, price, inventory, min, max, Integer.parseInt(machineId));
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(newPart);
                } else {
                    OutsourcedPart newPart = new OutsourcedPart(selectedPart.getId(), name, price, inventory, min, max, machineId);
                    Inventory.deletePart(selectedPart);
                    Inventory.addPart(newPart);
                }
                Common.changeScene("MainScreen", ModifyPartSave);
            }

        } catch (Exception e) {
            Common.throwError("Looks like some fields don't have the correct data types.");
        }

    }

    /**
     * Cancels and returns to the main screen
     * @throws IOException
     */
    public void ModifyPartCancel() throws IOException {
        Common.changeScene("MainScreen", ModifyPartCancel);
    }


}
