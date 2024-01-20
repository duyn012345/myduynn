package myduynn.myduynn;
import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    public static Connection getConnetion() {
        Connection databaseLink = null;
        String databaseName = "master";
        String databaseUser = "sa";
        String databasePassword = "12345";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=12345;encrypt=false";
        setEncrypt(false);
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
    private static void setEncrypt(boolean b) {
    }
    //public static Connection getConnection() {
   //     return null;
    //}
}
