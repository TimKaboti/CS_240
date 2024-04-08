package server.websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import dataAccess.*;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import webSocketMessages.serverMessages.Notification;
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

  private final ConnectionManager connections = new ConnectionManager(new WebSocketClient());
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
                try(PreparedStatement whiteRemoval = connection.prepareStatement("UPDATE  gamedata set whiteUsername = ? WHERE gameID = ?")){
                whiteRemoval.setString(1, null);
                whiteRemoval.setInt(2, gameID);
                whiteRemoval.executeUpdate();}
              }
              if(Objects.equals(name, blackUser)){
                try(PreparedStatement blackRemoval = connection.prepareStatement("UPDATE  gamedata set blackUsername = ? WHERE gameID = ?")){
                  blackRemoval.setString(1, null);
                  blackRemoval.setInt(2, gameID);
                  blackRemoval.executeUpdate();}
              }
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        } catch (SQLException | DataAccessException e) {
          throw new RuntimeException(e);
        }
        break;
      case RESIGN:
        Resign resign = new Gson().fromJson(message, Resign.class);
        String auth = resign.getAuthString();
        Integer iD =resign.getGameID();

        break;
      case MAKE_MOVE:
//        if game over == true throw error
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);

        break;
      case JOIN_PLAYER:
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
        break;
      case JOIN_OBSERVER:
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        break;
    }
        notificationHandler.notify();
//I may not need this notification handler.
    }





  }
