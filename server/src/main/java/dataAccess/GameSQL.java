package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.sql.*;
import java.util.List;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class GameSQL implements GameDAO{

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
    public void clear() throws DataAccessException {
        Connection connection;
        try {
            // Establishing a connection to the database
            connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE gameData");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void createGame() throws DataAccessException {

    }

    @Override
    public ChessGame getGame(Integer ID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame() throws DataAccessException {

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException {

    }

    @Override
    public boolean isNull(Integer gameID) throws DataAccessException {
        return false;
    }

    @Override
    public boolean taken(String color, Integer gameID) throws DataAccessException {
        return false;
    }
}
