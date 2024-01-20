package myduynn.myduynn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class addController  {
    @FXML
    private Button Home;
    @FXML
    private Button AddT;
    @FXML
    private Button Add;
    @FXML
    private Button them;
    @FXML
    private ChoiceBox<String> ChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField Price;
    @FXML
    private TextField Note;
    private Connection connection;
    @FXML
    private TableView<QL_ThuChii> manage;
    private ObservableList<QL_ThuChii> dataList;
    private PreparedStatement preparedStatement;

    @FXML
    public void initialize() {
        connection = DatabaseConnection.getConnetion();
        String pattern = "yyyy/MM/dd";
        datePicker.setPromptText(pattern.toLowerCase());
         DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        datePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        ChoiceBox.getItems().addAll("Thu", "Chi");
        ChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Lựa chọn mới: " + newValue);
        });
        dataList = FXCollections.observableArrayList();
    }
    public void connectHomeButtonOnAction(ActionEvent e) throws IOException {
        Stage stage = (Stage) Home.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 580);
        stage.setScene(scene);
        stage.show();
    }
    public void connectAddTButtonOnAction(ActionEvent e) throws IOException {
        Stage stage = (Stage) AddT.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 580);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void handleAddButtonAction(ActionEvent event) {
        DatePicker date = datePicker;
        String type = ChoiceBox.getValue();
        String note = Note.getText();
        try {
            String priceText = Price.getText().trim();
            if (!priceText.isEmpty()) {
                int price = Integer.parseInt(priceText);
                addData(date, type, price, note);
                showAlert("add ", "Data added successfully.", Alert.AlertType.INFORMATION);
                loadDataFromDatabase();
                manage.setItems(dataList);
            } else {
                showAlert("Error", "Price cannot be empty.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("Error", "Invalid price format.", Alert.AlertType.ERROR);
        }
    }
    private void addData(DatePicker date, String type, int price, String note) {
        try {
            if (connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=12345;encrypt=false", "sa", "12345");
                connection.setAutoCommit(true);
            }
            String query = "INSERT INTO QL_ThuChii (Date, Type, Price, Note) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setDate(1, Date.valueOf(date.getValue()));
                preparedStatement.setString(2, type);
                preparedStatement.setInt(3, price);
                preparedStatement.setString(4, note);
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {

                    System.out.println("Dữ liệu đã được thêm vào csdl.");
                } else {
                    System.out.println("Không có dliệu nào được thêm vào csdl.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(" ", "Lỗi khi thêm dữ liệu: " + ex.getMessage(), Alert.AlertType.ERROR);
        }finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    @FXML
    private void handlethemButtonAction(ActionEvent event) {
           try {
           String priceText = Price.getText().trim(); // Lấy giá trị từ TextField và loại bỏ khoảng trắng
            if (!priceText.isEmpty()) {
                try {
                    int price = Integer.parseInt(priceText);
                    Price.clear();
                    Note.clear();
                    datePicker.setValue(null);
                    ChoiceBox.getSelectionModel().clearSelection();// Xóa lựa chọn trong ChoiceBox (Type)
                    loadDataFromDatabase();
                    manage.setItems(dataList);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("error", " ", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
           e.printStackTrace();
        }
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void loadDataFromDatabase() {
        dataList.clear();
        try {
            connection = DatabaseConnection.getConnetion();
            String query = "SELECT * FROM QL_ThuChii";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int tt = resultSet.getInt("TT");
                    LocalDate date = resultSet.getDate("Date").toLocalDate();
                    String type = resultSet.getString("Type");
                    int price = resultSet.getInt("Price");
                    String note = resultSet.getString("Note");
                    QL_ThuChii ql_thuchi = new QL_ThuChii(tt,date, type, price, note);
                    dataList.add(ql_thuchi);
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
    }


}
