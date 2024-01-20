package myduynn.myduynn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;

public class dasboardController {
    @FXML
    private Button Dasboard;
    @FXML
    private Button Home;
    @FXML
    private Button AddT;
    @FXML
    private Button Exit;
    private Connection connection;

    public void connectHomeButtonOnAction(ActionEvent e)throws IOException {
        Stage stage=(Stage) Home.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));
        Scene scene=new Scene(fxmlLoader.load(),740,580);
        stage.setScene(scene);
        stage.show();
    }
    public void connectAddTButtonOnAction(ActionEvent e)throws IOException {
        Stage stage=(Stage) AddT.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add.fxml"));
        Scene scene =new Scene(fxmlLoader.load(),740,580);
        stage.setScene(scene);
        stage.show();
    }
    public void connectDasboardButtonOnAction(ActionEvent e)throws IOException {
        Stage stage=(Stage) Dasboard.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dasboard.fxml"));
        Scene scene =new Scene(fxmlLoader.load(),740,580);
        stage.setScene(scene);
        stage.show();
    }
    public void btnexit() {
        System.exit(0);

    }

}
