package webSocketMessages.serverMessages;

import java.util.Objects;

public class Error extends ServerMessage{
  private final String errorMessage;

  public Error( String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessge() {
    return errorMessage;
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

//  @Override
//  public String toString() {
//    return "Error{" +
//            "errorMessge='" + errorMessage + '\'' +
//            '}';
//  }
}
