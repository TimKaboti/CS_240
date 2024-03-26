package webSocketMessages.serverMessages;

import chess.ChessGame;

import java.util.Objects;

public class LoadGame extends ServerMessage {
  private final ChessGame game;

  public LoadGame(ServerMessageType type, ChessGame game) {
    super(type);
    this.game = game;
  }

  public ChessGame getGame() {
    return game;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof LoadGame loadGame)) return false;
    if (!super.equals(o)) return false;
    return Objects.equals(getGame(), loadGame.getGame());
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getGame());
  }

  @Override
  public String toString() {
    return "LoadGame{" +
            "game=" + game +
            '}';
  }
}
