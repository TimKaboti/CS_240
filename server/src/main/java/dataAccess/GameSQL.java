package dataAccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;
import org.springframework.util.SerializationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameSQL implements GameDAO {

  public void checkConnection() {
    try {
      // Establishing a connection
      Connection connection=DatabaseManager.getConnection();

      // Creating a statement
      Statement statement=connection.createStatement();

      // Executing a SELECT query
      ResultSet resultSet=statement.executeQuery("SELECT * FROM gamedata");

      // Processing the result set
      while (resultSet.next()) {
        // Access columns by name or index
        int id=resultSet.getInt("id");
        String name=resultSet.getString("name");

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
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("TRUNCATE TABLE gameData")) {

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new DataAccessException("unable to clear");
    }
  }


  @Override
  public void createGame(Integer ID, GameData data) throws DataAccessException {
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO gameData (gameID, gamename, game) VALUES (?, ?, ?)")) {

      Gson serializer=new Gson();
      String serializedGame=serializer.toJson(data);

      preparedStatement.setInt(1, ID);
      preparedStatement.setString(2, data.getGameName());
      preparedStatement.setString(3, serializedGame);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
      throw new DataAccessException("unable to create");
    }

  }

  @Override
  public ChessGame getGame(Integer ID) throws DataAccessException {
    ChessGame chessGame=null;

    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("SELECT game FROM gamedata WHERE gameID = ?")) {
      preparedStatement.setInt(1, ID);

      try (ResultSet result=preparedStatement.executeQuery()) {
        if (result.next()) {
          Gson serializer=new Gson();
          chessGame=serializer.fromJson(result.getString("game"), ChessGame.class);
        }
      }

    } catch (SQLException e) {
      throw new DataAccessException("unable to getGame");
    }

    return chessGame;
  }

  @Override
  public List<GameData> listGames() throws DataAccessException {
    List<GameData> temp=new ArrayList<>() {
    };
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM gamedata")) {

      try (ResultSet result=preparedStatement.executeQuery()) {
        // Use result.next() to check if there is any result
        while (result.next()) {
          GameData data=new GameData(result.getInt("gameID"), result.getString("whiteUsername"), result.getString("blackUsername"), result.getString("gameName"), null);
          temp.add(data);

        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("unable to list games");
    }
    return temp;
  }

  @Override
  public void updateGame(ChessGame game, ChessMove move, Integer gameID) throws DataAccessException, InvalidMoveException {
    // Update board and change team turn
    ChessGame.TeamColor team=game.getTeamTurn();
    game.makeMove(move);
    game.setTeamTurn(team);
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement statement=connection.prepareStatement("UPDATE gamedata SET game = ? WHERE gameID = ?")) {
      // Convert game object to byte array to store in the database
      String serialisedGame = new Gson().toJson(game);
      statement.setString(1, serialisedGame);
      statement.setInt(2, gameID);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Failed to update this game in the database.");
    }
  }

  @Override
  public void joinGame(String username, String color, Integer gameID) throws DataAccessException {
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("SELECT game FROM gamedata WHERE gameID = ?")) {
      color=color.toUpperCase();
      preparedStatement.setInt(1, gameID);
      try (ResultSet result=preparedStatement.executeQuery()) {
        // Use result.next() to check if there is any result
        if (result.next()) {
          if (color.equals("BLACK")) {
            try (PreparedStatement subStatement=connection.prepareStatement("UPDATE gamedata SET blackUsername = ? WHERE gameID = ?")) {
              subStatement.setString(1, username);
              subStatement.setInt(2, gameID);
              subStatement.executeUpdate();
            }
          } else if (color.equals("WHITE")) {
            try (PreparedStatement subStatement=connection.prepareStatement("UPDATE gamedata SET whiteUsername = ? WHERE gameID = ?")) {
              subStatement.setString(1, username);
              subStatement.setInt(2, gameID);
              subStatement.executeUpdate();
            }
          }
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("unable to join game");
    }
  }

  @Override
  public boolean isNull(Integer gameID) throws DataAccessException {
    return false;
  }


  @Override
  /**
   * returns true if a game with the provided gameID exists.
   * false otherwise.
   */ public boolean isNotNull(Integer gameID) throws DataAccessException {
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("SELECT game FROM gamedata WHERE gameID = ?")) {

      preparedStatement.setInt(1, gameID);
      try (ResultSet result=preparedStatement.executeQuery()) {
        // Return the result of result.next() directly
        return result.next();
      }

    } catch (SQLException e) {
      throw new DataAccessException("unable to confirm isNull.");
    }
  }


  @Override
  /**
   * returns a true if the desired color already has a username associated with it.
   * returns false otherwise.
   */ public boolean taken(String color, Integer gameID) throws DataAccessException {
    boolean bool=false;
    color=color.toUpperCase();
    try (Connection connection=DatabaseManager.getConnection()) {
      if (color.equals("BLACK")) {
        try (PreparedStatement preparedStatement=connection.prepareStatement("SELECT blackUsername FROM gamedata WHERE gameID = ?")) {
          preparedStatement.setInt(1, gameID);
          try (ResultSet result=preparedStatement.executeQuery()) {
            if (result.next()) {
              if (result.getString("blackUsername") != null) {
                bool=true;
              }
            }
          }
        }
      }

      if (color.equals("WHITE")) {
        try (PreparedStatement preparedStatement=connection.prepareStatement("SELECT whiteUsername FROM gamedata WHERE gameID = ?")) {
          preparedStatement.setInt(1, gameID);
          try (ResultSet result=preparedStatement.executeQuery()) {
            if (result.next()) {
              if (result.getString("whiteUsername") != null) {
                bool=true;
              }
            }
          }
        }
      }


      return bool;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public String whitePlayerName(Integer gameID) {
    String playerName=null;
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("SELECT whiteUsername FROM gamedata WHERE gameID = ?")) {
      preparedStatement.setInt(1, gameID);
      try (ResultSet set=preparedStatement.executeQuery()) {
        if (set.next()) {
          playerName=set.getString("whiteUsername");
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return playerName;
  }

  public String blackPlayerName(Integer gameID) {
    String playerName=null;
    try (Connection connection=DatabaseManager.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT blackUsername FROM gamedata WHERE gameID = ?")) {
      preparedStatement.setInt(1, gameID);
      try (ResultSet set=preparedStatement.executeQuery()) {
        if (set.next()) {
          playerName = set.getString("blackUsername");
        }
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
    return playerName;
  }

  public void setWhitePlayerNull(Integer gameID) {
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("UPDATE gamedata set whiteUsername = ? WHERE gameID = ?")) {
      preparedStatement.setInt(2, gameID);
      preparedStatement.setString(1, null);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void setBlackPlayerNull(Integer gameID) {
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement("UPDATE  gamedata set whiteUsername = ? WHERE gameID = ?")) {
      preparedStatement.setInt(2, gameID);
      preparedStatement.setString(1, null);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateGameState(ChessGame game, Integer gameID) throws DataAccessException {
    // Update board and change team turn
    ChessGame.TeamColor team=game.getTeamTurn();
    game.setTeamTurn(team);
    try (Connection connection=DatabaseManager.getConnection(); PreparedStatement statement=connection.prepareStatement("UPDATE gamedata SET game = ? WHERE gameID = ?")) {
      // Convert game object to byte array to store in the database
      String serialisedGame = new Gson().toJson(game);
      statement.setString(1, serialisedGame);
      statement.setInt(2, gameID);
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException("Failed to update this game in the database.");
    }
  }


}


