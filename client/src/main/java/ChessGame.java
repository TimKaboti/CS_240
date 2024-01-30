import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

public class ChessGame {

    public ChessGame() {
        board = new ChessBoard();
        turn = chess.ChessGame.TeamColor.WHITE;
    }

    ChessBoard board;
    chess.ChessGame.TeamColor turn;

    private HashSet<ChessMove> validMoves(ChessPosition position){
        return null;
    }

    private void makeMove(ChessMove move) {}

    private boolean isInCheck(chess.ChessGame.TeamColor teamColor) {
        return false;
    }

    private boolean isInCheckmate(chess.ChessGame.TeamColor teamColor) {
        return false;
    }

    private boolean isInStalemate(chess.ChessGame.TeamColor teamColor) {
        return false;
    }

    private void setBoard(ChessBoard board) {
        board.resetBoard();
    }

    public ChessBoard getBoard() {
        return board;
    }
}
