import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
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
        ChessPosition position = null;
        HashSet<ChessMove> opponentMoves = new HashSet<>();
        boolean inCheck = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == turn && board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                    position = new ChessPosition(i, j);
                } else if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != turn) {
                    opponentMoves.addAll(board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j)));
                }

            }
        }
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition() == position) {
                inCheck = true;
                break;
            }
        }
        return inCheck;
    }

    private boolean isInCheckmate(chess.ChessGame.TeamColor teamColor) {
        ChessPosition position = null;
        HashSet<ChessMove> opponentMoves = new HashSet<>();
        HashSet<ChessMove> myMoves = new HashSet<>();
        boolean inCheckMate = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == turn && board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                    position = new ChessPosition(i, j);
                } else if (board.getPiece(new ChessPosition(i, j)).getTeamColor() != turn) {
                    opponentMoves.addAll(board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j)));
                }

            }
        }
        myMoves.addAll(board.getPiece(new ChessPosition(position.getRow(), position.getColumn())).pieceMoves(board, position));
        for (ChessMove enemyMove : opponentMoves) {
            for (ChessMove myMove : myMoves) {
                if (enemyMove.getEndPosition() == myMove.getEndPosition()) {
                    inCheckMate = true;
                    break;
                }
            }
        }
        return inCheckMate;
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
