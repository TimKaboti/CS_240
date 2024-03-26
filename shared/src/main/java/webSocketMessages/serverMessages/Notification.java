package webSocketMessages.serverMessages;

import java.util.Objects;

public class Notification extends ServerMessage{
  private final String message;

  public Notification(ServerMessageType type, String message) {
    super(type);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Notification that)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(getMessage(), that.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getMessage());
  }

  @Override
  public String toString() {
    return "Notification{" +
            "message='" + message + '\'' +
            '}';
  }

}
