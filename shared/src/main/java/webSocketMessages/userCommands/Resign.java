package webSocketMessages.userCommands;

import java.util.Objects;

public class Resign extends UserGameCommand {


  private final int gameID;

  public Resign(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Resign resign)) return false;
    if (!super.equals(o)) return false;
    return getGameID() == resign.getGameID();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGameID());
  }

  @Override
  public String toString() {
    return "Resign{" +
            "gameID=" + gameID +
            '}';
  }
}
