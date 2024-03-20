package Result;

import chess.ChessBoard;

public record JoinGameResult(ChessBoard board, String message) {
    public JoinGameResult(ChessBoard board, String message) {
        this.message = message;
        this.board = board;
    }

    public String toString(){
        return this.message;
    }
}
