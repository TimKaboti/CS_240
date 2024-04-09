package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public Session session;

  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String userName, Session session) {
    var connection=new Connection(userName, session);
    connections.put(userName, connection);
  }

  public void remove(String visitorName) {
    connections.remove(visitorName);
  }

  public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
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
