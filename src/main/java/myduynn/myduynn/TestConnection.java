package myduynn.myduynn;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class TestConnection {
    public static void main(String[] args) {

        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser("sa");
        ds.setPassword("12345");
        ds.setServerName("LAPTOP-92B9ELOH\\DUYNMAST");
        ds.setPortNumber(1433);
        ds.setDatabaseName("master");
        ds.setEncrypt(false);
        try (Connection conn = ds.getConnection()) {
            System.out.println("Thanh Cong");
            System.out.println(conn.getMetaData());

        } catch (SQLServerException throwables) {
            throwables.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            ;
        }
    }

   // private static class SQLServerDataSource { public Connection getConnection() {}
    }

