package webSocketMessages.userCommands;

import java.util.Objects;

public class Leave extends UserGameCommand {
  private final int gameID;

  public Leave(String authToken, int gameID) {
    super(authToken);
    this.commandType = CommandType.LEAVE;
    this.gameID = gameID;
  }

  public int getGameID() {
    return gameID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Leave leave)) return false;
    if (!super.equals(o)) return false;
    return getGameID() == leave.getGameID();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGameID());
  }

  @Override
  public String toString() {
    return "Leave{" +
            "gameID=" + gameID +
            '}';
  }

}
