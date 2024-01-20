package myduynn.myduynn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalDateStringConverter;

public class ManageController {
    @FXML
    private TableView<QL_ThuChii> manage;
    @FXML
    private TableColumn<QL_ThuChii, Integer> TT;
    @FXML
    private TableColumn<QL_ThuChii, LocalDate> Date;
    @FXML
    private TableColumn<QL_ThuChii, String> Type;
    @FXML
    private TableColumn<QL_ThuChii, Integer> Price;
    @FXML
    private TableColumn<QL_ThuChii, String> Note;
    private ObservableList<QL_ThuChii> dataList = FXCollections.observableArrayList();
    @FXML
    private Button Home;
    @FXML
    private Button AddT;
    @FXML
    private Button Delete;
    @FXML
    private Button Edit;
    @FXML
    private Button comeback;
    @FXML
    private Button LogOut;
    @FXML
    private DatePicker DatePicker;
    private Connection connection;
    private Scene previousScene;
    private FilteredList<QL_ThuChii> filteredData;
    public void connectHomeButtonOnAction(ActionEvent e) throws IOException {
        previousScene = Home.getScene();
        Stage stage = (Stage) Home.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 580);
        stage.setScene(scene);
        stage.show();
    }
    public void buttonBackOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
            Parent root = loader.load();
            ManageController manageController = loader.getController();
            manageController.setPreviousScene(previousScene);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) manage.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    public void connectAddTButtonOnAction(ActionEvent e) throws IOException {
        Stage stage = (Stage) AddT.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 740, 580);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize() {
        connection = DatabaseConnection.getConnetion();
        TT.setCellValueFactory(new PropertyValueFactory<>("TT"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Note.setCellValueFactory(new PropertyValueFactory<>("Note"));
       // Date.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
//        Date.setOnEditCommit(event -> {
//            QL_ThuChii selectedItem = event.getRowValue();
//            selectedItem.setDate(event.getNewValue());
//            updateDataInTableView(selectedItem);
//        });
//        Type.setCellFactory(TextFieldTableCell.forTableColumn());
//        Type.setOnEditCommit(event -> {
//            QL_ThuChii selectedItem = event.getRowValue();
//            selectedItem.setType(event.getNewValue());
//            updateDataInTableView(selectedItem);
//        });
//        Price.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//        Price.setOnEditCommit(event -> {
//            QL_ThuChii selectedItem = event.getRowValue();
//            selectedItem.setPrice(event.getNewValue());
//            updateDataInTableView(selectedItem);
//        });
//        Note.setCellFactory(TextFieldTableCell.forTableColumn());
//        Note.setOnEditCommit(event -> {
//            QL_ThuChii selectedItem = event.getRowValue();
//            selectedItem.setNote(event.getNewValue());
//            updateDataInTableView(selectedItem);
//        });
        setupEditableColumns();
        loadDataFromDatabase();
        String pattern = "yyyy/MM/dd";
        DatePicker.setPromptText(pattern.toLowerCase());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        DatePicker.setConverter(new javafx.util.StringConverter<LocalDate>() {
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
        // Đặt cell factory cho cột ngày
        Date.setCellFactory(col -> {
            TableCell<QL_ThuChii, LocalDate> cell = new TableCell<QL_ThuChii, LocalDate>() {
                private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(formatter.format(item));
                    }
                }
            };
            return cell;
        });
        TT.setSortType(TableColumn.SortType.ASCENDING);
        TT.setComparator((tt1, tt2) -> Integer.compare(tt1, tt2));
        manage.getSortOrder().add(TT);
        manage.sort();
        Delete.setOnAction(this::handleDeleteButtonAction);

    }
    private void setupEditableColumns() {
        Date.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        Date.setOnEditCommit(event -> handleEditCommit(event, "Date"));
        Type.setCellFactory(TextFieldTableCell.forTableColumn());
        Type.setOnEditCommit(event -> handleEditCommit(event, "Type"));
        Price.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        Price.setOnEditCommit(event -> handleEditCommit(event, "Price"));
        Note.setCellFactory(TextFieldTableCell.forTableColumn());
        Note.setOnEditCommit(event -> handleEditCommit(event, "Note"));
        manage.setEditable(true);
    }
    private void handleEditCommit(TableColumn.CellEditEvent<QL_ThuChii, ?> event, String columnName) {
        QL_ThuChii selectedItem = event.getRowValue();
        Object newValue = event.getNewValue();
        switch (columnName) {
            case "Date":
                selectedItem.setDate((LocalDate) newValue);
                break;
            case "Type":
                selectedItem.setType((String) newValue);
                break;
            case "Price":
                selectedItem.setPrice((Integer) newValue);
                break;
            case "Note":
                selectedItem.setNote((String) newValue);
                break;
            default:
                break;
        }
        updateDataInTableView(selectedItem);
    }
    @FXML
    public void handleDeleteButtonAction(ActionEvent event) {
        QL_ThuChii selectedItem = manage.getSelectionModel().getSelectedItem();
        int selectedIndex = manage.getSelectionModel().getSelectedIndex();
        if (selectedItem != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa dữ liệu?");
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                deleteData(selectedItem);
                try {
                    connection = DatabaseConnection.getConnetion();
                    String resetIdentityQuery = "DBCC CHECKIDENT ('QL_ThuChii', RESEED, 0)";
                    try (PreparedStatement resetIdentityStatement = connection.prepareStatement(resetIdentityQuery)) {
                        resetIdentityStatement.executeUpdate();
                    }
                    loadDataFromDatabase();
                    manage.setItems(dataList);
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Lỗi", "Vui lòng chọn một hàng để xóa.", Alert.AlertType.ERROR);
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
        } else {
            showAlert("Lỗi", "Không có dữ liệu để xóa.", Alert.AlertType.ERROR);
        }
    }
    private void deleteData(QL_ThuChii data) {
        try {
            if (connection.isClosed()) {
                connection = DatabaseConnection.getConnetion();
            }
            if (deleteDataFromDatabase(data)) {
                dataList.remove(data);
                manage.setItems(dataList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể xóa dữ liệu từ csdl.", Alert.AlertType.ERROR);
        }
    }
    private boolean deleteDataFromDatabase(QL_ThuChii data) throws SQLException {
        String query = "DELETE FROM QL_ThuChii WHERE TT = ? AND Date = ? AND Type = ? AND Price = ? AND Note = ?";
        try (
                //Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, data.getTT());
            preparedStatement.setDate(2, java.sql.Date.valueOf(data.getDate())); // Replace ID with the actual ID field in your data object
            preparedStatement.setString(3, data.getType());
            preparedStatement.setInt(4, data.getPrice());
            preparedStatement.setString(5, data.getNote());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Dữ liệu đã được xóa thành công từ csdl.");
                return true;
            } else {
                System.out.println("Không có dữ liệu nào được xóa từ csdl.");
                return false;
            }
        }
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        try {
            LocalDate selectedDate = DatePicker.getValue();
            String formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String sql = "SELECT * FROM QL_ThuChii WHERE Date = ?";
            manage.getItems().clear();
            try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=12345;encrypt=false", "sa", "12345");
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, formattedDate);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int tt = resultSet.getInt("TT");
                        java.sql.Date sqlDate = resultSet.getDate("Date");
                        // Convert java.sql.Date sang java.time.LocalDate
                        LocalDate date = sqlDate.toLocalDate();
                        String type = resultSet.getString("Type");
                        int price = resultSet.getInt("Price");
                        String note = resultSet.getString("Note");
                        manage.getItems().add(new QL_ThuChii(tt, date, type, price, note));

                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void LogOutButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Controller Controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            // Đóng giao diện quản lý thư viện
            ((Stage) manage.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void editButtonOnAction(ActionEvent event) {
        QL_ThuChii selectedItem = manage.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            showEditDialog(selectedItem);
        } else {
            System.out.println("Please select a row to edit.");
        }
    }
    private void showEditDialog(QL_ThuChii itemToEdit) {
        Dialog<QL_ThuChii> dialog = new Dialog<>();
        dialog.setTitle("Edit Data");
        dialog.setHeaderText("Edit the selected item:");

        DatePicker datePicker = new DatePicker(itemToEdit.getDate());
        TextField priceField = new TextField(String.valueOf(itemToEdit.getPrice()));
        TextField noteField = new TextField(itemToEdit.getNote());
        TextField typeField = new TextField(itemToEdit.getType());

        datePicker.setPromptText("Edit Date");
        priceField.setPromptText("Edit Price");
        noteField.setPromptText("Edit Note");
        typeField.setPromptText("Edit Type");
        // Thêm các trường nhập liệu vào cửa sổ dialog
        GridPane grid = new GridPane();
        grid.add(new Label("Date:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Type:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Note:"), 0, 3);
        grid.add(noteField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                LocalDate newDate = datePicker.getValue();
                String newType = typeField.getText();
                int newPrice = Integer.parseInt(priceField.getText());
                String newNote = noteField.getText();
                QL_ThuChii updatedItem = new QL_ThuChii(itemToEdit.getTT(), newDate, itemToEdit.getType(), newPrice, newNote);
                updateDataInTableView(updatedItem);
                return updatedItem;
            }
            return null;
        });
        Optional<QL_ThuChii> result = dialog.showAndWait();
        result.ifPresent(updatedItem -> {
        });

    }
    private void updateDataInTableView(QL_ThuChii updatedItem) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DatabaseConnection.getConnetion();
            }
            String query = "UPDATE QL_ThuChii SET Date=?, Type=?, Price=?, Note=? WHERE TT=?";// WHERE Date=? AND Type=? AND Price=? AND Note=?
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, java.sql.Date.valueOf(updatedItem.getDate()).toString());
                preparedStatement.setString(2, updatedItem.getType());//updatedItem.getType()
                preparedStatement.setInt(3, updatedItem.getPrice());
                preparedStatement.setString(4, updatedItem.getNote());
                preparedStatement.setInt(5, updatedItem.getTT());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Dữ liệu đã được cập nhật thành công.");
                } else {
                    System.out.println("Không có dữ liệu nào được cập nhật.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Lỗi SQL: " + ex.getMessage());
        }
    }
    private void loadDataFromDatabase() {
        try {
            //connection = DatabaseConnection.getConnection();
            String query = "SELECT * FROM QL_ThuChii";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    java.sql.Date sqlDate = resultSet.getDate("Date");
                    int tt = resultSet.getInt("TT");
                    LocalDate date = sqlDate.toLocalDate();
                    String type = resultSet.getString("Type");
                    int price = resultSet.getInt("Price");
                    String note = resultSet.getString("Note");
                    QL_ThuChii ql_thuchi = new QL_ThuChii(tt, date, type, price, note);// dataList.add(ql_thuchi);
                    manage.getItems().add(ql_thuchi);
                }
                System.out.println("Items in TableView: " + manage.getItems().size());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}