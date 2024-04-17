package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinPlayer extends UserGameCommand {

  private final int gameID;
  private final ChessGame.TeamColor playerColor;

  public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(authToken);
    this.commandType = CommandType.JOIN_PLAYER;
    this.gameID = gameID;
    this.playerColor = playerColor;
  }

  public int getGameID() {
    return gameID;
  }

  public ChessGame.TeamColor getPlayerColor() {
    return playerColor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JoinPlayer that)) return false;
    if (!super.equals(o)) return false;
    return getGameID() == that.getGameID() && Objects.equals(getPlayerColor(), that.getPlayerColor());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGameID(), getPlayerColor());
  }

  @Override
  public String toString() {
    return "JoinPlayer{" +
            "gameID=" + gameID +
            ", playerColor='" + playerColor + '\'' +
            '}';
  }
  public String message(){
    return playerColor.toString();
  }
}
