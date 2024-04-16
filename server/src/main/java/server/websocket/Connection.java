package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;
import java.util.Objects;

public class Connection {
  public String userName;
  public Session session;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Connection that)) return false;
    return Objects.equals(userName, that.userName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userName, session);
  }

  public Connection(String userName, Session session) {
    this.userName = userName;
    this.session = session;
  }

  public void send(String msg) throws IOException {
    session.getRemote().sendString(msg);
  }
}
