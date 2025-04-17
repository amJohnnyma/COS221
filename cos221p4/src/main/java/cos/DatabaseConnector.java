package cos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseConnector {


    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(DBConfig.DB_URL, DBConfig.DB_USER, DBConfig.DB_PASSWORD);
            System.out.println("Connected to database successfully!");
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
