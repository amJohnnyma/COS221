package cos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;


public class DatabaseConnector {


    private Connection connection;

    public void connect() {
        try {
            // Establish the connection
            connection = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
            System.out.println("Connected to database successfully! " + DBConfig.DB_URL);
    
            /* 
            // Retrieve and display the current database name
            String currentDatabase = connection.getCatalog();
            System.out.println("Current database: " + currentDatabase);
    
            // Query the information_schema to get all table names
            String query = "SHOW TABLES";
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("Tables in the database:");
                while (rs.next()) {
                    System.out.println(rs.getString(1)); // Table name
                }
            }
            */
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    
    public List<String> handleQuery(String query) {
        List<String> names = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("product_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }


}
