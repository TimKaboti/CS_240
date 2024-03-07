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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM authData");

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
        String authToken = null;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT authToken FROM authData");
             ResultSet resultSet = selectStatement.executeQuery()) {

            // Collect authTokens from the ResultSet
            while (resultSet.next()) {
                authToken = UUID.randomUUID().toString();
                String existingAuthToken = resultSet.getString("authToken");

                // Check if the generated authToken already exists
                if (existingAuthToken.equals(authToken)) {
                    // If the authToken exists, generate a new one
                    continue;
                } else {
                    // If the authToken is unique, break out of the loop
                    break;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO authData (username, authToken) VALUES (?, ?)")) {

            insertStatement.setString(1, username);
            insertStatement.setString(2, authToken);
            insertStatement.executeUpdate();

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
        boolean bool = false;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT authToken FROM authData WHERE authToken = ?")) {

            preparedStatement.setString(1, token);
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                if (result.next()) {
                    bool = true;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bool;
    }

    public String getUsername(String token) throws DataAccessException {
        String name = null;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM authData WHERE authToken = ?")) {

            preparedStatement.setString(1, token);
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                if (result.next()) {
                    name = result.getNString("username");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return name;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM authData WHERE authToken = ?")) {

            preparedStatement.setString(1, token);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE authData")) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
