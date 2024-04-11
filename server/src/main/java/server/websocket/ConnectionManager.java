package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public Session session;

  public final ConcurrentHashMap<Integer, HashSet<Connection>> connections = new ConcurrentHashMap<>();

  public void add(Integer gameID, String userName, Session session) {
    HashSet<Connection> connectionSet = connections.get(gameID);
    var connection=new Connection(userName, session);
    if(connectionSet == null){
      connectionSet = new HashSet<>();
      connections.put(gameID, connectionSet);
      connectionSet.add(connection);
    } else {
      connectionSet.add(connection);
    }
  }

  public void remove(Integer gameID, String userName, Session session) {
    HashSet<Connection> connectionSet = connections.get(gameID);
    var connection= new Connection(userName, session);
    if(connectionSet != null){
      if(connectionSet.contains(connection)){
        connectionSet.remove(connection);
      }
    }
  }
//all but the client.
  public void broadcast(Integer gameID, String excludeVisitorName, Session session, Notification notification) throws IOException {
    HashSet<Connection> connectionSet = connections.get(gameID);
    List<Connection> sendList = new ArrayList<>();
    for (var c : connectionSet) {
      if (c.session.isOpen()) {
        if (c.session != session) {
          sendList.add(c);
        }
      }
    }
    for (var c : sendList){
      c.send(notification.toString());
    }
  }

  public void connection(Session session) {
      this.session=session;
    }
}
