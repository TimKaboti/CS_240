package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
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
//ask who broadcast is supposed to be sending notifications to. is it all but the client or just the client?
  public void broadcast(Integer gameID, String excludeVisitorName, Notification notification) throws IOException {
    var removeList=new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.userName.equals(excludeVisitorName)) {
          c.send(notification.toString());
        }
      } else {
        removeList.add(c);
      }
    }
  }

  public void connection(Session session) {
      this.session=session;
    }
}
