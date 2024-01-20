module myduynn.myduynn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
    requires com.microsoft.sqlserver.jdbc;

    opens myduynn.myduynn to javafx.fxml;
    exports myduynn.myduynn;
}