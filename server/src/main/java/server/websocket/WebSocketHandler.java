package server.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import dataAccess.*;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import server.websocket.ConnectionManager;
import org.springframework.util.SerializationUtils;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.MessageHandler;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();
  NotificationHandler notificationHandler;


  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch(command.getCommandType()){
      case LEAVE:
        Leave leave = new Gson().fromJson(message, Leave.class);
        String authToken = leave.getAuthString();
        Integer gameID = leave.getGameID();
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM userData WHERE authToken = ?")){
          statement.setString(1, authToken);
          String name = String.valueOf(statement.executeQuery());
          try(
              PreparedStatement firstStatement = connection.prepareStatement("SELECT whiteUsername FROM gameData WHERE gameID = ?");
              PreparedStatement secondStatement = connection.prepareStatement("SELECT blackUsername FROM gameData WHERE gameID = ?")
              ){
            firstStatement.setInt(1,gameID);
            secondStatement.setInt(1,gameID);
            String whiteUser = String.valueOf(firstStatement.executeQuery());
            String blackUser = String.valueOf(secondStatement.executeQuery());
              if(Objects.equals(name, whiteUser)){
                try(PreparedStatement whiteRemoval = connection.prepareStatement("UPDATE  gameData set whiteUsername = ? WHERE gameID = ?")){
                whiteRemoval.setString(1, null);
                whiteRemoval.setInt(2, gameID);
                whiteRemoval.executeUpdate();}
              }
              if(Objects.equals(name, blackUser)){
                try(PreparedStatement blackRemoval = connection.prepareStatement("UPDATE  gameData set blackUsername = ? WHERE gameID = ?")){
                  blackRemoval.setString(1, null);
                  blackRemoval.setInt(2, gameID);
                  blackRemoval.executeUpdate();}
              }
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }

          String webMessage = name + " has left the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
//          probably need a Leave Notification too.
        } catch (SQLException | DataAccessException e) {
          throw new RuntimeException(e);
        }
        // need to add the appropriate message around here. maybe before the catch block.
        break;
      case RESIGN:
        Resign resign = new Gson().fromJson(message, Resign.class);
        authToken = resign.getAuthString();
        gameID =resign.getGameID();
        String playerName;
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM userData WHERE authToken = ?")){
          statement.setString(1, authToken);
          playerName = String.valueOf(statement.executeQuery());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        try(Connection connection = DatabaseManager.getConnection();
          PreparedStatement statement = connection.prepareStatement("SELECT game from gameData WHERE gameID = ?")){
          statement.setInt(1, gameID);
          ChessGame thisGame =(ChessGame) statement.executeQuery();
          thisGame.setGameState(true);
          byte[] gameBytes=SerializationUtils.serialize(thisGame);
          PreparedStatement updateGame=connection.prepareStatement("UPDATE gameData SET game = ? WHERE gameID = ?");
          updateGame.setInt(2, gameID);
          updateGame.setBytes(1, gameBytes);
          updateGame.executeUpdate();
          String webMessage = playerName + " has chosen to resign. This game is over." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
//          probably still need a Resign notification here as well.
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
// need to add the appropriate message around here. maybe before the catch block.
        break;
      case MAKE_MOVE:
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
        authToken = makeMove.getAuthString();
        gameID = makeMove.getGameID();
        String thisName;
        String whitePlayer;
        String blackPlayer;
        ChessPosition start = makeMove.getMove().getStartPosition();
        ChessPosition end = makeMove.getMove().getEndPosition();
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM userData WHERE authToken = ?")){
          statement.setString(1, authToken);
          thisName = String.valueOf(statement.executeQuery());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT blackUsername FROM gameData WHERE gameID = ?");
            PreparedStatement secondStatement = connection.prepareStatement("SELECT whiteUsername FROM gameData WHERE gameID = ?")){
          statement.setInt(1, gameID);
          secondStatement.setInt(1,gameID);
          blackPlayer = String.valueOf(statement.executeQuery());
          whitePlayer = String.valueOf(secondStatement.executeQuery());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT game FROM gameData WHERE gameID = ?")
        ) {
          statement.setInt(1, gameID);
          ChessGame thisGame = (ChessGame) statement.executeQuery();
          if(thisGame.getGameState()){
//            need to write the return message here. also probably need to break too.
            break;
          } else {
//            here im checking for if the game is in checkmate, but I likely need to check for just check as well.
            thisGame.makeMove(makeMove.getMove());
            ChessPiece piece =  thisGame.getBoard().getPiece(end);
            if (thisGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
              thisGame.setGameState(true);
//              new Notification
              String webMessage = blackPlayer + " is in Checkmate." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);

            } else if (thisGame.isInCheck(ChessGame.TeamColor.BLACK)) {
//              new Notification
              String webMessage = blackPlayer + " is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);

            }
            if (thisGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            thisGame.setGameState(true);
//              new Notification
              String webMessage = whitePlayer + " is in Checkmate." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);

            }else if (thisGame.isInCheck(ChessGame.TeamColor.WHITE)) {
//              new Notification
              String webMessage = whitePlayer + " is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);

            }
            byte[] gameBytes=SerializationUtils.serialize(thisGame);
            PreparedStatement updateGame=connection.prepareStatement("UPDATE gameData SET game = ? WHERE gameID = ?");
            updateGame.setInt(2, gameID);
            updateGame.setBytes(1, gameBytes);
            updateGame.executeUpdate();

            String webMessage = thisName + " has moved "+ piece.getPieceType().toString() + " from " + start + " to " + end + "." ;
//            may need to mess with the toStrings to get this to print properly. also may need to do something like coordConvert for start and end.
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          }

        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
          throw new RuntimeException(e);
        }
// need to add the appropriate message around here. maybe before the catch block.
        break;
      case JOIN_PLAYER:
        String name;
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        authToken = joinPlayer.getAuthString();
        gameID =joinPlayer.getGameID();
        String color =joinPlayer.getPlayerColor();
        ChessGame game;
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM userData WHERE authToken = ?")){
//          I may also need to select the ChessGame itself so that the LoadGame instance can use it.
          statement.setString(1, authToken);
          name = String.valueOf(statement.executeQuery());} catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        if(color.equalsIgnoreCase("WHITE")){
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE gameData set whiteUsername = ? where gameID = ?");
            PreparedStatement secondStatement = connection.prepareStatement("SELECT game FROM gameData where gameID = ?")){
          statement.setString(1, name);
          statement.setInt(2, gameID);
          statement.executeUpdate();
          secondStatement.setInt(1, gameID);
          game = (ChessGame) secondStatement.executeQuery();
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
          }
          String webMessage = name + " has joined the game as White Player." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
//        create new Notification
//        create new loadGame
// need to add the appropriate message around here. maybe before the catch block.
        }
        if(color.equalsIgnoreCase("BLACK")) {
          try (Connection connection=DatabaseManager.getConnection();
               PreparedStatement statement=connection.prepareStatement("UPDATE gameData set blackUsername = ? where gameID = ?");
               PreparedStatement secondStatement = connection.prepareStatement("SELECT game FROM gameData where gameID = ?")) {
            statement.setString(1, name);
            statement.setInt(2, gameID);
            statement.executeUpdate();
            secondStatement.setInt(1, gameID);
            game = (ChessGame) secondStatement.executeQuery();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          } catch (DataAccessException e) {
            throw new RuntimeException(e);
          }
          String webMessage=name + " has joined the game as Black Player.";
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
//        create new Notification
//        create new loadGame
        }
        break;
      case JOIN_OBSERVER:
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        authToken = joinObserver.getAuthString();
        gameID =joinObserver.getGameID();
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT name FROM userData WHERE authToken = ?")){
          statement.setString(1, authToken);
          String observerName = String.valueOf(statement.executeQuery());
        String webMessage = observerName + " has joined the game as an Observer." ;
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
//        need to create the connections map so that a person observing the game can be added to it.
//        also need to add players to that map just like the observers, so I need to add them in the 'join_Game' case
        // need to add the appropriate message around here.

        break;
    } catch (SQLException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
//        notificationHandler.notify();
    }

  }
}
