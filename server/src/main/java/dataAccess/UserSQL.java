package dataAccess;
import model.UserData;

import java.sql.*;
import java.sql.Connection;

public class UserSQL implements UserDAO{

    public UserSQL(){
    }

    public void checkConnection() {
        try {
            // Establishing a connection
            Connection connection = DatabaseManager.getConnection();

            // Creating a statement
            Statement statement = connection.createStatement();

            // Executing a SELECT query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM userData");

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
    /**
     * Returns the userName from the database if the username can be found there.
     * in short, determines if there is an account associated with given username.
     ******might want to make this return a boolean. you only need to know if it
     * already exits
     * @PARAM String username
     */
    public boolean getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM userData WHERE username = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    // Compare the retrieved username with the input username
                    String retrievedUsername = result.getString("username");
                    return username.equals(retrievedUsername);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to get user");
        }
        return false;
    }



    @Override
    /**
     * returns the password associated with a given username.
     *
     * @PARAM String username
     */
    public String getPassword(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM userData WHERE username = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return result.getString("password");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to get password");
        }
        return null;
    }


    public String getEmail(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT email FROM userData WHERE username = ?")) {

            preparedStatement.setString(1, username);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    return result.getString("email");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to get email");
        }
        return null;
    }



    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("unable to create user");
        }
    }


    @Override
    public void clear() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE userData")) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("unable to clear");
        }
    }



//    private int executeUpdate(String statement, Object... params) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (var i = 0; i < params.length; i++) {
//                    var param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException e) {
//            return -1;
//        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
