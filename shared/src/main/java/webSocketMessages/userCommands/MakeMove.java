package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMove extends UserGameCommand {
  private final int gameID;
  private final ChessMove move;

  public MakeMove(String authToken, int gameID, ChessMove move) {
    super(authToken);
    this.commandType = CommandType.MAKE_MOVE;
    this.gameID = gameID;
    this.move = move;
  }

  public int getGameID() {
    return gameID;
  }

  public ChessMove getMove() {
    return move;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MakeMove makeMove)) return false;
    if (!super.equals(o)) return false;
    return getGameID() == makeMove.getGameID() && Objects.equals(getMove(), makeMove.getMove());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGameID(), getMove());
  }

  @Override
  public String toString() {
    return "MakeMove{" +
            "gameID=" + gameID +
            ", move=" + move +
            '}';
  }
}
