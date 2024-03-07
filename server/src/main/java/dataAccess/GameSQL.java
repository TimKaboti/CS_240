package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.sql.*;
import java.util.List;

public class GameSQL implements GameDAO {

    public void checkConnection() {
        try {
            // Establishing a connection
            Connection connection = DatabaseManager.getConnection();

            // Creating a statement
            Statement statement = connection.createStatement();

            // Executing a SELECT query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM gameData");

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
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE gameData")) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void createGame(Integer ID, GameData data) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO gameData (gameID, gamename, game) VALUES (???)")) {

            preparedStatement.setInt(1, ID);
            preparedStatement.setString(2, data.getGameName());
            preparedStatement.setBlob(3, (Blob) data.getGame());
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                if (result.next()) {
                    //chessGame = (ChessGame) result.getObject("game");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ChessGame getGame(Integer ID) throws DataAccessException {
        ChessGame chessGame = null;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT game FROM gameData WHERE gameID = ?")) {

            preparedStatement.setInt(1, ID);
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                if (result.next()) {
                    chessGame = (ChessGame) result.getObject("game");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return chessGame;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        List<GameData> temp = null;
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from = gameData")) {

            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                while (result.next()) {
                    GameData data = new GameData(result.getInt("gameID"), result.getString("whiteUsername"), result.getString("blackUsername"), result.getString("gameName"), null);
                    if (temp != null) {
                        temp.add(data);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

    @Override
    public void updateGame() throws DataAccessException {

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT game FROM gameData WHERE gameID = ?")) {

            preparedStatement.setInt(1, gameID);
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Use result.next() to check if there is any result
                if (result.next()) {
                    if (color.equals("BLACK")) {
                        try (PreparedStatement subStatement = connection.prepareStatement("UPDATE gameData SET blackUsername = ? WHERE gameID = ?")) {
                            subStatement.setString(1, username);
                            subStatement.setInt(2, gameID);
                            subStatement.executeUpdate();
                        }
                    } else if (color.equals("WHITE")) {
                        try (PreparedStatement subStatement = connection.prepareStatement("UPDATE gameData SET whiteUsername = ? WHERE gameID = ?")) {
                            subStatement.setString(1, username);
                            subStatement.setInt(2, gameID);
                            subStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    /**
     * returns true if a game with the provided gameID exists.
     * false otherwise.
     */
    public boolean isNull(Integer gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT game FROM gameData WHERE gameID = ?")) {

            preparedStatement.setInt(1, gameID);
            try (ResultSet result = preparedStatement.executeQuery()) {
                // Return the result of result.next() directly
                return result.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    /**
     * returns a true if the desired color already has a username associated with it.
     * returns false otherwise.
     */
    public boolean taken(String color, Integer gameID) throws DataAccessException {
        boolean bool = false;

        try (Connection connection = DatabaseManager.getConnection()) {
            if (color.equals("BLACK")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT blackUsername FROM gameData WHERE gameID = ?")) {
                    preparedStatement.setInt(1, gameID);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            bool = true;
                        }
                    }
                }
            }

            if (color.equals("WHITE")) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT whiteUsername FROM gameData WHERE gameID = ?")) {
                    preparedStatement.setInt(1, gameID);
                    try (ResultSet result = preparedStatement.executeQuery()) {
                        if (result.next()) {
                            bool = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bool;
    }

}


