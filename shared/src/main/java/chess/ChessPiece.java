package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (board.getPiece(myPosition).getPieceType()) {
            case KING:
                kingMoves(board, myPosition);
                break;

//            case QUEEN -> ;

//            case BISHOP -> ;

//            case KNIGHT -> ;

//            case ROOK -> ;

            case PAWN:
                pawnMoves(board, myPosition);
                break;


        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return Objects.equals(type, that.type) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }

    public void kingMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        if (board.getPiece(new ChessPosition(x + 1, y)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x - 1, y)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x, y + 1)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x, y-1)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x+1, y+1)) == null || board.getPiece(new ChessPosition(x+1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x-1, y-1)) == null || board.getPiece(new ChessPosition(x+1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x+1, y-1)) == null || board.getPiece(new ChessPosition(x+1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x-1, y+1)) == null || board.getPiece(new ChessPosition(x+1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
    }

    public void pawnMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        if (board.getPiece(new ChessPosition(x + 1, y)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.ROOK));
        }
        if (board.getPiece(new ChessPosition(x + 2, y)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.ROOK));
        }
        if (board.getPiece(new ChessPosition(x + 1, y+1)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.ROOK));
        }
        if (board.getPiece(new ChessPosition(x + 1, y-1)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.ROOK));
        }

    }


}