package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

/**
 * Common functions that can be shared across multiple methods
 */
public class Common {
    /**
     * Changes scenes based on a file name and button performing the action
     * @param fileName
     * @param button
     * @throws IOException
     */
    public static void changeScene(String fileName, Button button) throws IOException {
        Parent root = FXMLLoader.load(Common.class.getResource("/view/" + fileName + ".fxml"));
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Throws an error
     * @param errorMessage
     */
    public static void throwError(String errorMessage) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(errorMessage);
        errorAlert.showAndWait();
    }

    /**
     * Throws an error asking user for confirmation
     * @param confirmation
     * @return
     */
    public static boolean getConfirmation(String confirmation) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText(confirmation);
        Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
        return confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK;
    }


}
