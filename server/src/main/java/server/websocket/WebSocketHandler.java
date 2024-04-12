package server.websocket;

import chess.*;
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

  GameDAO gameDao;
  AuthDAO authDao;

  public WebSocketHandler(AuthDAO authDao, GameDAO gameDao){
    this.authDao = authDao;
    this.gameDao = gameDao;
  }
  private final ConnectionManager connections = new ConnectionManager();
  NotificationHandler notificationHandler;


  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException, DataAccessException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch(command.getCommandType()){
      case LEAVE:
        Leave leave = new Gson().fromJson(message, Leave.class);
        String authToken = leave.getAuthString();
        Integer gameID = leave.getGameID();
        String name = null;
        String whiteUser = null;
        String blackUser = null;
        try{name = authDao.getUsername(authToken);} catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        try{whiteUser = gameDao.whitePlayerName(gameID);} catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        try{blackUser = gameDao.blackPlayerName(gameID);} catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
        if(name == whiteUser){
          try{gameDao.setWhitePlayerNull(gameID);
          String tempMessage = name + " has left the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, tempMessage);
//          broadcast
          connections.remove(gameID, name, session);
          connections.broadcast(gameID,name,session,notification);} catch (DataAccessException e) {
            throw new RuntimeException(e);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        if(name == blackUser){
          try{gameDao.setWhitePlayerNull(gameID);
          String tempMessage = name + " has left the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, tempMessage);
//          broadcast
          connections.remove(gameID, name, session);
          connections.broadcast(gameID,name,session,notification);} catch (DataAccessException e) {
            throw new RuntimeException(e);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }



        // need to add the appropriate message around here. maybe before the catch block.
        break;
      case RESIGN:
        Resign resign = new Gson().fromJson(message, Resign.class);
        authToken = resign.getAuthString();
        gameID =resign.getGameID();
        String playerName=null;
        try{playerName = authDao.getUsername(authToken);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve player's name to resign.");
          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }

        try{ChessGame thisGame = gameDao.getGame(gameID);
          thisGame.setGameState(true);
          String webMessage = playerName + " has chosen to resign. This game is over." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
//          send to all.
          connections.broadcast(gameID,playerName,session,notification);

        } catch (DataAccessException e) {
          Error error = new Error("Error: failed to return approppriate resign message.");
          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
// need to add the appropriate message around here. maybe before the catch block.
        break;
      case MAKE_MOVE:
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
        authToken = makeMove.getAuthString();
        gameID = makeMove.getGameID();
        String thisName = null;
        String whitePlayer = null;
        String blackPlayer = null;
        ChessPosition start = makeMove.getMove().getStartPosition();
        ChessPosition end = makeMove.getMove().getEndPosition();
        ChessGame game = gameDao.getGame(gameID);
        ChessMove move = makeMove.getMove();
        try{thisName = authDao.getUsername(authToken);} catch (DataAccessException e){
          Error error = new Error("Error: failed to retrieve game in joinPlayer.");
          connections.broadcast(gameID, thisName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        try{gameDao.updateGame(game, move, gameID);} catch (
                InvalidMoveException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          Error error = new Error("Error: failed to make the requested move.");
          connections.broadcast(gameID, thisName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        try{whitePlayer = gameDao.whitePlayerName(gameID);} catch (DataAccessException e){}
        try{blackPlayer = gameDao.blackPlayerName(gameID);} catch (DataAccessException e){}
          if(game.getGameState()){
            Error error = new Error("Error: cannot make a move after game is over or a player has resigned.");
            connections.broadcast(gameID, thisName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
            break;
          } else {
            ChessPiece piece =  game.getBoard().getPiece(end);
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
              game.setGameState(true);
              String webMessage = "Black is in Checkmate." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,blackPlayer,session,notification);

            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
              String webMessage = "Black is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,blackPlayer,session,notification);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            game.setGameState(true);
              String webMessage = "White is in Checkmate." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,whitePlayer,session,notification);

            }else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
              String webMessage = "White is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,whitePlayer,session,notification);
            }

            String webMessage = thisName + " has moved "+ piece.getPieceType().toString() + " from " + start.toString() + " to " + end.toString() + "." ;
//            may need to mess with the toStrings to get this to print properly. also may need to do something like coordConvert for start and end.
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
            connections.broadcast(gameID,thisName,session,notification);
//broadcast(also may need to be moved.)
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,game);
            connections.broadcast(gameID, thisName, session, loadGame);
            String newLoad = new Gson().toJson(loadGame);
            send(session, newLoad);
// send to all.
          }
// need to add the appropriate message around here. maybe before the catch block.
        break;
      case JOIN_PLAYER:
        String tempName =null;
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        authToken = joinPlayer.getAuthString();
        gameID =joinPlayer.getGameID();
        String color =joinPlayer.getPlayerColor();
        ChessGame thisGame=null;
        try{name = authDao.getUsername(authToken);} catch(DataAccessException e){
          Error error = new Error("Error: Failed to obtain users name.");
          connections.broadcast(gameID, tempName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        if(color.equalsIgnoreCase("WHITE")){
        try{gameDao.joinGame(tempName, color, gameID);} catch(DataAccessException e){
          Error error = new Error("Error: failed to set user as white player.");
          connections.broadcast(gameID, tempName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          throw new RuntimeException(e);
          }
        try{thisGame = gameDao.getGame(gameID);} catch (DataAccessException e){
          Error error = new Error("Error: failed to retrieve game in joinPlayer.");
          connections.broadcast(gameID, tempName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
          connections.add(gameID, tempName, session);
          String webMessage = tempName + " has joined the game as White Player." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          connections.broadcast(gameID,tempName,session,notification);
//          broadcast
          LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,thisGame);
          String newLoad = new Gson().toJson(loadGame);
          send(session, newLoad);

        }
        if(color.equalsIgnoreCase("BLACK")) {
          try{gameDao.joinGame(tempName, color, gameID);} catch(DataAccessException e){
            Error error = new Error("Error: failed to set user as black player.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
            throw new RuntimeException(e);
          }
          try{thisGame = gameDao.getGame(gameID);} catch (DataAccessException e){
            Error error = new Error("Error: failed to retrieve game in joinPlayer.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
          }
          connections.add(gameID, tempName, session);
          String webMessage = tempName + " has joined the game as Black Player." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          connections.broadcast(gameID,tempName,session,notification);
//          broadcast
          LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,thisGame);
          String newLoad = new Gson().toJson(loadGame);
          send(session, newLoad);

        }
        break;
      case JOIN_OBSERVER:
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        authToken = joinObserver.getAuthString();
        gameID =joinObserver.getGameID();
        ChessGame tempGame=null;
        String observerName=null;
        try{observerName = authDao.getUsername(authToken);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve observer's name from database.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        try{gameDao.joinGame(observerName, null, gameID);} catch (DataAccessException e){
          Error error = new Error("Error: failed to set this user as an observer to the chosen game.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        try{tempGame = gameDao.getGame(gameID);} catch (DataAccessException e){
          Error error = new Error("Error: failed to retrieve game in join Observer.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
          connections.add(gameID, observerName, session);
//          add this observer to the hashmap
        String webMessage = observerName + " has joined the game as an Observer." ;
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          connections.broadcast(gameID,observerName,session,notification);
//        broadcast
        LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, tempGame);
          String newLoad = new Gson().toJson(loadGame);
          send(session, newLoad);
        break;
    }

  }

  public void send(Session session,String msg) throws IOException {
    session.getRemote().sendString(msg);
  }
}

