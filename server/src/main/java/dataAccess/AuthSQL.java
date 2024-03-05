package dataAccess;

import model.AuthData;

import java.sql.*;

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
        return null;
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

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
