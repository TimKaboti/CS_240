package webSocketMessages.serverMessages;

import java.util.Objects;

public class Error extends ServerMessage{
  private final String errorMessge;

  public Error(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessge = errorMessage;
  }

  public String getErrorMessge() {
    return errorMessge;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Error error)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(getErrorMessge(), error.getErrorMessge());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getErrorMessge());
  }

  @Override
  public String toString() {
    return "Error{" +
            "errorMessge='" + errorMessge + '\'' +
            '}';
  }
}
