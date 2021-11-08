package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHousePart;
import model.Inventory;
import model.OutsourcedPart;
import model.Product;

/**
 * Main class for the inventory program
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setTitle("Main Screen");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Launches the inventory program
     * @param args
     *
     * JavaDoc files can be found in the JavaDocFiles directory under the main Software I PA folder.
     *
     * Comments regarding error resolution and future enhancements can be found in the classes within the controller
     * folder.
     *
     */
    public static void main(String[] args) {
        addTestData();
        launch(args);
    }

    /**
     * Adds test data to ensure program is working correctly
     */
    private static void addTestData() {
        // Initializing test data
        InHousePart chassis = new InHousePart(Inventory.generateId("part"), "Chassis", 100, 10, 0, 25, 1);
        InHousePart engine = new InHousePart(Inventory.generateId("part"), "Engine", 1000, 10, 0, 25, 2);
        OutsourcedPart wheels = new OutsourcedPart(Inventory.generateId("part"), "Wheels", 250, 10, 0, 25, "Seller1");
        Product motorcycle = new Product(Inventory.generateId("product"), "Motorcycle", 5000, 5, 0, 10);
        Product secondMotorcycle = new Product(Inventory.generateId("product"), "MoarMotorcycle", 5500, 2, 0, 10);
        Product thirdMotorcycle = new Product(Inventory.generateId("product"), "Brotorcycle", 500, 2, 0, 10);
        Inventory.addPart(chassis);
        Inventory.addPart(engine);
        Inventory.addPart(wheels);
        motorcycle.addAssociatedPart(chassis);
        motorcycle.addAssociatedPart(engine);
        motorcycle.addAssociatedPart(wheels);
        Inventory.addProduct(motorcycle);
        Inventory.addProduct(secondMotorcycle);
        Inventory.addProduct(thirdMotorcycle);
    }
}
