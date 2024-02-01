package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        board = new ChessBoard();
        turn = WHITE;
    }
    ChessBoard board;
    TeamColor turn;


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> opponentMoves = new HashSet<>();
        HashSet<ChessMove> myMoves = new HashSet<>();
        if (board.getPiece(startPosition) != null){
            myMoves.addAll(board.getPiece(startPosition).pieceMoves(board, startPosition));
        }
        TeamColor myColor;
        if (board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn())) == null) {
            myMoves = null;
        } else {
            myColor = board.getPiece(new ChessPosition(startPosition.getRow(), startPosition.getColumn())).getTeamColor();
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() != myColor) {
                        opponentMoves.addAll(board.getPiece(startPosition).pieceMoves(board, new ChessPosition(i, j)));
                    }
                }
            }
        }
        for (ChessMove enemyMove : opponentMoves){
            for (ChessMove myMove : myMoves){
                if(enemyMove.getEndPosition().equals(myMove.getEndPosition()) && board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING){
                    myMoves.remove(myMove);
                }
            }
        }
        return myMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        HashSet<ChessMove> valid_moves = new HashSet<>();
        ChessPosition start_position = move.getStartPosition();
        valid_moves.addAll(validMoves(start_position));
        ChessPiece this_piece = new ChessPiece(board.getPiece(start_position).getTeamColor(),board.getPiece(start_position).getPieceType());
        if (!valid_moves.contains(move)){
            throw new InvalidMoveException("Move does not Exist");
        } else if (getTeamTurn() == this_piece.getTeamColor()) {
            board.deletePiece(start_position);
            board.addPiece(move.getEndPosition(),this_piece);
            if (this_piece.getTeamColor() == WHITE) {
                turn = BLACK;
            }
            if (this_piece.getTeamColor() == BLACK) {
                turn = WHITE;
            }
        } else {
            throw new InvalidMoveException("Move does not Exist");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition position = null;
        HashSet<ChessMove> opponentMoves = new HashSet<>();
        boolean inCheck = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor
                        && board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                    position = new ChessPosition(i, j);
                } else if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    opponentMoves.addAll(board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j)));
                }

            }
        }
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(position)) {
                inCheck = true;
                break;
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition position = null;
        HashSet<ChessMove> opponentMoves = new HashSet<>();
        HashSet<ChessMove> myMoves = new HashSet<>();
        boolean inCheckMate = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor
                        && board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                    position = new ChessPosition(i, j);
                } else if (board.getPiece(new ChessPosition(i, j)) != null && board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    opponentMoves.addAll(board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j)));
                }

            }
        }
        if (isInCheck(teamColor)) {
            myMoves.addAll(board.getPiece(new ChessPosition(position.getRow(), position.getColumn())).pieceMoves(board, position));
            for (ChessMove enemyMove : opponentMoves) {
                for (ChessMove myMove : myMoves) {
                    if (enemyMove.getEndPosition().equals(myMove.getEndPosition())) {
                        inCheckMate = true;
                        break;
                    }
                }
            }
        }
        return inCheckMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return Objects.equals(getBoard(), chessGame.getBoard()) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), turn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", turn=" + turn +
                '}';
    }
}
