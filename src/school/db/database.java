package school.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    public Connection connectionDatabase() {
        String url = "jdbc:sqlserver://192.168.100.29:1434;databaseName=schooltest6;integratedSecurity=true;trustServerCertificate=true;encrypt=true;";

        try {
            Connection connection = DriverManager.getConnection(url);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // return null if connection fails
        }
    }
}
