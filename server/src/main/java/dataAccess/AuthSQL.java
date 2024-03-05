package dataAccess;

import java.sql.*;
import java.util.UUID;

public class AuthSQL implements AuthDAO{

    public void checkConnection() {
        try {
            // Establishing a connection
            Connection connection = DatabaseManager.getConnection();

            // Creating a statement
            Statement statement = connection.createStatement();

            // Executing a SELECT query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table");

            // Processing the result set
            while (resultSet.next()) {
                // Access columns by name or index
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                // Process the data as needed
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // Closing resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String CreateAuth(String username) throws DataAccessException {
        Connection connection;
        String authToken = null;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT authToken FROM authData");
            ResultSet tableResult = preparedStatement.executeQuery();
            // Collect authTokens from the ResultSet
            while (tableResult.next()) {
                authToken = UUID.randomUUID().toString();
                String existingAuthToken = tableResult.getString("authToken");
                // Check if the generated authToken already exists
                if (existingAuthToken.equals(authToken)) {
                    // If the authToken exists, generate a new one
                    continue;
                } else {
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO authData (username, authToken) VALUES (?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);preparedStatement.executeUpdate();
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    @Override
    /**
     * checks to see if the given auth can be found in the database authData table.
     */
    public boolean getAuth(String token) throws DataAccessException {
        Connection connection;
        boolean bool = false;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT authToken FROM authData WHERE authToken = ?");
            preparedStatement.setString(1, token);
            ResultSet result = preparedStatement.executeQuery();
            if (!result.wasNull()) {
                bool = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bool;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        Connection connection;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE authData SET authToken = ? WHERE authToken = ?");
            preparedStatement.setString(1, null);
            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        Connection connection;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE authData");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
