package dataAccess;
import chess.ChessGame;
import model.UserData;
import dataAccess.DatabaseManager;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;

import static com.mysql.cj.conf.PropertyKey.PASSWORD;
import static com.mysql.cj.conf.PropertyKey.USER;
import static dataAccess.DatabaseManager.password;
import static dataAccess.DatabaseManager.user;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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
    /**
     * Returns the userName from the database if the username can be found there.
     * in short, determines if there is an account associated with given username.
     *
     * @PARAM String username
     */
    public UserData getUser(String username) throws DataAccessException {
//        SELECT username FROM userdata;
        return null;
    }

    @Override
    /**
     * returns the password associated with a given username.
     *
     * @PARAM String username
     */
    public String getPassword(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        Connection connection;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)");
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() throws DataAccessException {

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
