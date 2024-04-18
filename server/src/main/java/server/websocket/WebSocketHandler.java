package server.websocket;

import chess.*;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import dataAccess.*;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;


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
  public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
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
          Error error = new Error("Error: failed to retrieve player's name.");
          connections.broadcast(gameID, name, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        try{whiteUser = gameDao.whitePlayerName(gameID);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve white player's name.");
          connections.broadcast(gameID, name, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        try{blackUser = gameDao.blackPlayerName(gameID);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve black player's name.");
          connections.broadcast(gameID, name, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        HashSet<Connection> connectionSet = connections.get(gameID);
        var connection=new Connection(name, session);
        if(Objects.equals(name, whiteUser)){
          try{gameDao.setWhitePlayerNull(gameID);
          String tempMessage = name + " has left the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, tempMessage);
//          broadcast
          connections.remove(gameID, name, session);
          connections.broadcast(gameID,name,session,notification);
//          String note=new Gson().toJson(notification);
//          send(session, note);
          break;
          } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
          }
        }
        else if(Objects.equals(name, blackUser)){
          try{gameDao.setBlackPlayerNull(gameID);
          String tempMessage = name + " has left the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, tempMessage);
//          broadcast
          connections.remove(gameID, name, session);
          connections.broadcast(gameID,name,session,notification);
//            String note=new Gson().toJson(notification);
//            send(session, note);
          break;
          } catch (DataAccessException | IOException e) {
            throw new RuntimeException(e);
          }
        }
        else if(connectionSet.contains(connection)){
          connections.remove(gameID, name, session);
          String tempMessage = name + " has stopped observing the game." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, tempMessage);
          connections.broadcast(gameID,name,session,notification);
        }
        else {
          Error error = new Error("Error: the name given is not a participant nor a player.");
          connections.broadcast(gameID, name, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        // need to add the appropriate message around here. maybe before the catch block.
        break;
      case RESIGN:
        Resign resign = new Gson().fromJson(message, Resign.class);
        authToken = resign.getAuthString();
        gameID = resign.getGameID();
        String playerName=null;
        String blkPlyr = gameDao.blackPlayerName(gameID);
        String whtPlyr = gameDao.whitePlayerName(gameID);
        try{playerName = authDao.getUsername(authToken);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve player's name to resign.");
          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }

        ChessGame tmpGame = gameDao.getGame(gameID);
        if(tmpGame.getGameState()){
          Error error = new Error("Error: one player has aleady resigned.");
//          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }

        if(!Objects.equals(playerName, blkPlyr) && !Objects.equals(playerName, whtPlyr)){
          Error error = new Error("Error: an observer attempted to resign.");
//          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        try{ChessGame thisGame = gameDao.getGame(gameID);
          thisGame.setGameState(true);
          gameDao.updateGameState(thisGame, gameID);
          String webMessage = playerName + " has chosen to resign. This game is over." ;
          Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
          connections.broadcast(gameID,playerName,session,notification);
          String thisNote = new Gson().toJson(notification);
          send(session, thisNote);
        } catch (DataAccessException e) {
          Error error = new Error("Error: failed to return appropriate resign message.");
          connections.broadcast(gameID, playerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
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
        String teamColor = null;
        ChessPosition start = makeMove.getMove().getStartPosition();
        ChessPosition end = makeMove.getMove().getEndPosition();
        ChessGame game = gameDao.getGame(gameID);
        ChessMove move = makeMove.getMove();
        whitePlayer = gameDao.whitePlayerName(gameID);
        blackPlayer = gameDao.blackPlayerName(gameID);
        if(game.getGameState() == true){
          Error error = new Error("Error: cannot make a move after game is over or a player has resigned.");
//          connections.broadcast(gameID, thisName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;}
        try{thisName = authDao.getUsername(authToken);} catch (DataAccessException e){
          Error error = new Error("Error: failed to retrieve game in joinPlayer.");
          connections.broadcast(gameID, thisName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        if(Objects.equals(whitePlayer, thisName)){
          teamColor = "white";
        }
        if(Objects.equals(blackPlayer, thisName)){
          teamColor = "black";
        }
        if(!Objects.equals(thisName, blackPlayer) && !Objects.equals(thisName, whitePlayer)){
          Error error=new Error("Error: attempted to make move as an observer.");
//          connections.broadcast(gameID, thisName, session, error);
          String thisError=new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        if (teamColor != null && !teamColor.equalsIgnoreCase(game.getBoard().getPiece(move.getStartPosition()).getTeamColor().toString())) {
          Error error=new Error("Error: attempted to make move for other team.");
//          connections.broadcast(gameID, thisName, session, error);
          String thisError=new Gson().toJson(error);
          send(session, thisError);
          break;
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
            try{gameDao.updateGame(game, move, gameID);}
            catch (InvalidMoveException e) {
              Error error = new Error("Error: failed to make the requested move.");
//          connections.broadcast(gameID, thisName, session, error);
              String thisError = new Gson().toJson(error);
              send(session, thisError);
              break;
            } catch (Exception e) {e.printStackTrace();}
            ChessPiece piece = game.getBoard().getPiece(end);
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
              game.setGameState(true);
              gameDao.updateGame(game, move, gameID);
              String webMessage = "Black is in Checkmate. This game is over." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,blackPlayer,session,notification);

            } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
              String webMessage = "Black is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,blackPlayer,session,notification);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
              game.setGameState(true);
              gameDao.updateGame(game, move, gameID);
              String webMessage = "White is in Checkmate. This game is over." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,whitePlayer,session,notification);

            }else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
              String webMessage = "White is in Check." ;
              Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
              connections.broadcast(gameID,whitePlayer,session,notification);
            }

            String webMessage = thisName + " has moved "+ piece.getPieceType().toString() + " from " + start.toString() + " to " + end.toString() + "." ;
            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
            connections.broadcast(gameID,thisName,session,notification);
            ChessGame newGame = gameDao.getGame(gameID);
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME,newGame);
            connections.broadcast(gameID, thisName, session, loadGame);
            String newLoad = new Gson().toJson(loadGame);
            send(session, newLoad);
// send to all.
          }
// need to add the appropriate message around here. maybe before the catch block.
        break;
      case JOIN_PLAYER:
        String tempName = null;
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        authToken = joinPlayer.getAuthString();
        gameID =joinPlayer.getGameID();
        String color =joinPlayer.getPlayerColor().toString();
        ChessGame thisGame = null;
        String blkPlayer;
        String whtPlayer;
        if(gameDao.getGame(gameID) != null){
        try{
        blkPlayer = gameDao.blackPlayerName(gameID);
        whtPlayer = gameDao.whitePlayerName(gameID);

//         && !Objects.equals(tempName, blkPlayer)

          try{tempName = authDao.getUsername(authToken);} catch(DataAccessException e){
          Error error = new Error("Error: Failed to obtain users name.");
          connections.broadcast(gameID, tempName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
          if (tempName == null){
            Error error = new Error("Error: Failed to obtain users name.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
            break;}

        if(color.equalsIgnoreCase("WHITE")){
          if (Objects.equals(whtPlayer, null)){
            Error error=new Error("Error: player was never set as the white player. this spot s null.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError=new Gson().toJson(error);
            send(session, thisError);
            break;
          }
          if(Objects.equals(whtPlayer, tempName) && !Objects.equals(blkPlayer,tempName)) {
              try {
                gameDao.joinGame(tempName, color, gameID);
              } catch (DataAccessException e) {
                Error error=new Error("Error: failed to set user as white player.");
                connections.broadcast(gameID, tempName, session, error);
                String thisError=new Gson().toJson(error);
                send(session, thisError);
                break;
              }
              try {
                thisGame=gameDao.getGame(gameID);
              } catch (DataAccessException e) {
                Error error=new Error("Error: failed to retrieve game in joinPlayer.");
                connections.broadcast(gameID, tempName, session, error);
                String thisError=new Gson().toJson(error);
                send(session, thisError);
                break;
              } finally {
                connections.add(gameID, tempName, session);
                String webMessage=tempName + " has joined the game as White Player.";
                Notification notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
                connections.broadcast(gameID, tempName, session, notification);
                LoadGame loadGame=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, thisGame);
                String newLoad=new Gson().toJson(loadGame);
                send(session, newLoad);
            }
          } else {
            Error error = new Error("Error: Attempted to join as the wrong color.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
            break;
          }
        }

        if(color.equalsIgnoreCase("BLACK")) {
          if(Objects.equals(blkPlayer, null)){
            Error error=new Error("Error: player never set as black player. black is set to null.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError=new Gson().toJson(error);
            send(session, thisError);
            break;
          }
          if(Objects.equals(blkPlayer, tempName) && !Objects.equals(whtPlayer,tempName)) {
              try {
                gameDao.joinGame(tempName, color, gameID);
              } catch (DataAccessException e) {
                Error error=new Error("Error: failed to set user as black player.");
                connections.broadcast(gameID, tempName, session, error);
                String thisError=new Gson().toJson(error);
                send(session, thisError);
                break;
              }
              try {
                thisGame=gameDao.getGame(gameID);
              } catch (DataAccessException e) {
                Error error=new Error("Error: failed to retrieve game in joinPlayer.");
                connections.broadcast(gameID, tempName, session, error);
                String thisError=new Gson().toJson(error);
                send(session, thisError);
                break;
              } finally {
                connections.add(gameID, tempName, session);
                String webMessage=tempName + " has joined the game as Black Player.";
                Notification notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, webMessage);
                connections.broadcast(gameID, tempName, session, notification);
                LoadGame loadGame=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, thisGame);
                String newLoad=new Gson().toJson(loadGame);
                send(session, newLoad);
            }
          } else {
            Error error = new Error("Error: Attempted to join as the wrong color.");
            connections.broadcast(gameID, tempName, session, error);
            String thisError = new Gson().toJson(error);
            send(session, thisError);
            break;}
        }
        } catch (Exception e) {
//          e.printStackTrace();
        }
        break;}
        else{ Error error = new Error("Error: Incorrect gameID to join game.");
          connections.broadcast(gameID, tempName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;}
      case JOIN_OBSERVER:
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        authToken = joinObserver.getAuthString();
        gameID =joinObserver.getGameID();
        ChessGame tempGame=null;
        String observerName=null;
        if(gameDao.getGame(gameID) != null){

        try{observerName = authDao.getUsername(authToken);} catch (DataAccessException e) {
          Error error = new Error("Error: failed to retrieve observer's name from database.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
        }
        if(observerName == null){
          Error error = new Error("Error: failed to retrieve observer's name from database.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
        }
        try{tempGame = gameDao.getGame(gameID);} catch (DataAccessException e){
          Error error = new Error("Error: failed to retrieve game in join Observer.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;
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
        break;}
        else{ Error error = new Error("Error: Incorrect gameID.");
          connections.broadcast(gameID, observerName, session, error);
          String thisError = new Gson().toJson(error);
          send(session, thisError);
          break;}
    }

  }

  public void send(Session session,String msg) throws IOException {
    session.getRemote().sendString(msg);
  }
}

