package myduynn.myduynn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.*;

public class Controller {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    private Connection connection;
    public void loginButtonOnAction(ActionEvent e) {
        if (usernameTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
             //loginMessageLabel.setText("You try to login!");
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter username and pasword.");
        }
    }
    public void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    private void validateLogin() {
        try {
            connection = DatabaseConnection.getConnetion();

            // Prepare the SQL statement
            String query = "select count(*) from Users WHERE Username = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, usernameTextField.getText());
                preparedStatement.setString(2, passwordPasswordField.getText());
                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 1) {    // Check if the user exists
                       // loginMessageLabel.setText("Login successful");// User with the provided username and password exists
                        showAlert("Welcome!", "Successful login!", Alert.AlertType.INFORMATION);
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));// Additional logic for a successful login can be added here
                        Scene newScene = new Scene(fxmlLoader.load(), 730, 580);
                        Stage currentStage = (Stage) loginMessageLabel.getScene().getWindow();// Lấy stage hiện tại và thiết lập cảnh mới
                        currentStage.setScene(newScene);
                    } else {
                        loginMessageLabel.setText("Invalid username or password");// No user with the provided username and password
                    }
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }public void btnexit() {
        System.exit(0);
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

