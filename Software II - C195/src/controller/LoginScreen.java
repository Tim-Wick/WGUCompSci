package controller;

import com.mysql.cj.log.Log;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import jdbc.JDBC;
import main.Common;
import main.myLog;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class for the login screen. Includes username and password fields and error checking to ensure only authorized users
 * can access the application.
 */
public class LoginScreen implements Initializable {
    public TextField LoginUsername;
    public TextField LoginPassword;
    public Button LoginButton;
    public TextField LoginLocation;
    private static String currentUser;
    public Label LoginTitle;
    public Label LoginUserLabel;
    public Label LoginPassLabel;
    public Label LoginLocationLabel;

    /**
     * Initializes the LoginScreen
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoginLocation.setText(String.valueOf(ZoneId.systemDefault()));
        if (String.valueOf(Locale.getDefault().getLanguage()).equals("fr")) {
            ResourceBundle rb = ResourceBundle.getBundle("controller.Login");
            LoginTitle.setText(rb.getString("Title"));
            LoginUserLabel.setText(rb.getString("Username"));
            LoginUsername.setText(rb.getString("EnterUsername"));
            LoginPassLabel.setText(rb.getString("Password"));
            LoginPassword.setText(rb.getString("EnterPassword"));
            LoginLocationLabel.setText(rb.getString("Location"));
            LoginButton.setText(rb.getString("LoginLabel"));
        }

    }

    /**
     * Logs in to the application
     * @param mouseEvent The mouse being clicked.
     * @throws IOException, SQLException
     */
    public void Login(MouseEvent mouseEvent) throws IOException, SQLException {
        String username = LoginUsername.getText();
        String password = LoginPassword.getText();
        String errorMessage = "Username/Password incorrect!";
        if (String.valueOf(Locale.getDefault().getLanguage()).equals("fr")) {
            ResourceBundle rb = ResourceBundle.getBundle("controller.Login");
            errorMessage = rb.getString("error");
        }
        currentUser = username;
        ResultSet queryResults = JDBC.searchDB("select Password from USERS where User_Name =\"" + username + "\";");
        if (!queryResults.next()) {
            Common.throwError(errorMessage);
        }
        if (queryResults.getString(1).equals(password)) {
            myLog loginLog = new myLog(LoginUsername.getText(), true);
            loginLog.writeLog();
            Common.changeScene("MainScreen", LoginButton);
        } else {
            myLog loginLog = new myLog(LoginUsername.getText(), false);
            loginLog.writeLog();
            Common.throwError(errorMessage);
        }
    }

    /**
     * Gets the username of the current user
     * @return currentUser
     */
    public static String getCurrentUser() {
        return currentUser;
    }

}
