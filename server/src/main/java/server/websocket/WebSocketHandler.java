package server.websocket;

import com.google.gson.Gson;
import dataAccess.*;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
//import webSocketMessages.Action;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager(new WebSocketClient());

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
//   the message parameter, once parsed using toGson/toJson, should have information like a makeMove command ect.
//    what needs to happen is I need to get this method to somehow have access to the database so it can update.
    }


//  private void enter(String visitorName, Session session) throws IOException {
//    connections.add(visitorName, session);
//    var message = String.format("%s is in the shop", visitorName);
//    var notification = new Notification(Notification.Type.ARRIVAL, message);
//    connections.broadcast(visitorName, notification);
//  }

//  private void exit(String visitorName) throws IOException {
//    connections.remove(visitorName);
//    var message = String.format("%s left the shop", visitorName);
//    var notification = new Notification(Notification.Type.DEPARTURE, message);
//    connections.broadcast(visitorName, notification);
//  }


  }
