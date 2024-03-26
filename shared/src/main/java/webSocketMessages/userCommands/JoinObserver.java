package webSocketMessages.userCommands;

import java.util.Objects;

public class JoinObserver extends UserGameCommand {
  private final int gameID;

  public JoinObserver(String authToken, int gameID) {
    super(authToken);
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JoinObserver that)) return false;
    if (!super.equals(o)) return false;
    return getGameID() == that.getGameID();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGameID());
  }

  @Override
  public String toString() {
    return "JoinObserver{" +
            "gameID=" + gameID +
            '}';
  }
}
