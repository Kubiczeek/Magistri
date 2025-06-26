import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnector.class.getName());
    private final Connection connection;

    public DatabaseConnector(String url, String user, String password) {
        if (url == null || user == null || password == null) {
            throw new IllegalArgumentException("URL, user, and password must not be null");
        }
        if (url.isEmpty() || user.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("URL, user, and password must not be empty");
        }
        // If all checks pass, assign the values to the instance variables

        // Establish a connection to the database
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to the database", e);
            throw new RuntimeException("Failed to connect to the database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close database connection", e);
            }
        }
    }

    public void executeUpdate(String query, Object... params) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query must not be empty");
        }
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to execute update: " + query, e);
            throw new RuntimeException("Failed to execute update: " + e.getMessage());
        }
    }

    public ResultSet executeQuery(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query must not be empty");
        }
        try {
            return connection.createStatement().executeQuery(query);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to execute query: " + query, e);
            throw new RuntimeException("Failed to execute query: " + e.getMessage());
        }
    }

    public java.util.List<String[]> getAllUsers() {
        java.util.List<String[]> users = new java.util.ArrayList<>();
        String query = "SELECT first_name, last_name, date_of_birth, role FROM users";
        try (ResultSet rs = executeQuery(query)) {
            while (rs.next()) {
                users.add(new String[] {
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("date_of_birth"),
                    rs.getString("role")
                });
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to fetch users", e);
        }
        return users;
    }
}
