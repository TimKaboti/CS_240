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
                return kingMoves(board, myPosition);


            case QUEEN:
                return queenMoves(board, myPosition);


            case BISHOP:
                return bishopMoves(board, myPosition);

//            case KNIGHT -> ;

            case ROOK:
                return rookMoves(board, myPosition);

            case PAWN:
                return pawnMoves(board, myPosition);

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

    private HashSet<ChessMove> kingMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        if (board.getPiece(new ChessPosition(x + 1, y)) == null || board.getPiece(new ChessPosition(x + 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
        }
        if (board.getPiece(new ChessPosition(x - 1, y)) == null || board.getPiece(new ChessPosition(x - 1, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x -1 , y), null));
        }
        if (board.getPiece(new ChessPosition(x, y + 1)) == null || board.getPiece(new ChessPosition(x, y + 1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x, y +1), null));
        }
        if (board.getPiece(new ChessPosition(x, y-1)) == null || board.getPiece(new ChessPosition(x, y-1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x, y-1), null));
        }
        if (board.getPiece(new ChessPosition(x+1, y+1)) == null || board.getPiece(new ChessPosition(x+1, y+1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y+1), null));
        }
        if (board.getPiece(new ChessPosition(x-1, y-1)) == null || board.getPiece(new ChessPosition(x-1, y-1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x - 1, y-1), null));
        }
        if (board.getPiece(new ChessPosition(x+1, y-1)) == null || board.getPiece(new ChessPosition(x+1, y-1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 1, y-1), null));
        }
        if (board.getPiece(new ChessPosition(x-1, y+1)) == null || board.getPiece(new ChessPosition(x-1, y+1)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x - 1, y+1), null));
        }
        return myHash;
    }
//    public static HashSet<ChessMoves>
    private HashSet<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        if (myColor == ChessGame.TeamColor.WHITE) {
//            if (board.getPiece(new ChessPosition(x + 1, y)) == null) {
//                myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
//            }

//            if (board.getPiece(new ChessPosition(x + 1, y + 1)) != null && board.getPiece(new ChessPosition(x + 1, y + 1)).getTeamColor() != myColor) {
//                myHash.add(new ChessMove(position, new ChessPosition(x + 1, y + 1), null));
//            }

//            if (board.getPiece(new ChessPosition(x + 1, y - 1)) != null && board.getPiece(new ChessPosition(x + 1, y - 1)).getTeamColor() != myColor) {
//                myHash.add(new ChessMove(position, new ChessPosition(x + 1, y - 1), null));
//            }
            if (board.getPiece(new ChessPosition(x + 1, y)) == null) {
                if (x+1 == 8) {
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), PieceType.QUEEN));
                }
                else{
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
                }
            }
            if (board.getPiece(new ChessPosition(x + 1, y+1)) != null && board.getPiece(new ChessPosition(x + 1, y + 1)).getTeamColor() != myColor) {
                if (x+1 == 8) {
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y+1), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y+1), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y+1), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y+1), PieceType.QUEEN));
                }else{
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y + 1), null));
                }
            }
            if (board.getPiece(new ChessPosition(x + 1, y-1)) != null && board.getPiece(new ChessPosition(x + 1, y - 1)).getTeamColor() != myColor) {
                if (x+1 == 8) {
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y-1), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y-1), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y-1), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y-1), PieceType.QUEEN));
                }
                else{
                    myHash.add(new ChessMove(position, new ChessPosition(x + 1, y - 1), null));
                }
            }
        }
        if (myColor == ChessGame.TeamColor.BLACK) {
//            if (board.getPiece(new ChessPosition(x -1 , y)) == null || board.getPiece(new ChessPosition(x - 1, y)).getTeamColor() != myColor) {
//                myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), null));
//            }

//            if (board.getPiece(new ChessPosition(x - 1, y + 1)) != null && board.getPiece(new ChessPosition(x - 1, y + 1)).getTeamColor() != myColor) {
//                myHash.add(new ChessMove(position, new ChessPosition(x - 1, y + 1), null));
//            }

//            if (board.getPiece(new ChessPosition(x - 1, y - 1)) != null && board.getPiece(new ChessPosition(x - 1, y - 1)).getTeamColor() != myColor) {
//                myHash.add(new ChessMove(position, new ChessPosition(x - 1, y - 1), null));
//            }
            if (board.getPiece(new ChessPosition(x - 1, y)) == null || board.getPiece(new ChessPosition(x - 1, y)).getTeamColor() != myColor) {
                if (x - 1 == 1) {
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), PieceType.QUEEN));
                }
                else{
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), null));
                }
            }
            if (board.getPiece(new ChessPosition(x - 1, y+1)) != null && board.getPiece(new ChessPosition(x - 1, y + 1)).getTeamColor() != myColor) {
                if (x -1 == 1) {
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y+1), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y+1), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y+1), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y+1), PieceType.QUEEN));
                }
                else{
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y + 1), null));
                }
            }

            if (board.getPiece(new ChessPosition(x - 1, y-1)) != null && board.getPiece(new ChessPosition(x - 1, y - 1)).getTeamColor() != myColor) {
                if (x -1 == 1) {
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y-1), PieceType.ROOK));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y-1), PieceType.KNIGHT));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y-1), PieceType.BISHOP));
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y-1), PieceType.QUEEN));
                }
                else{
                    myHash.add(new ChessMove(position, new ChessPosition(x - 1, y - 1), null));
                }
            }
        }

        if (myColor == ChessGame.TeamColor.WHITE && x == 2) {
            if (board.getPiece(new ChessPosition(x + 2, y)) == null && board.getPiece(new ChessPosition(x + 1, y)) == null) {
                myHash.add(new ChessMove(position, new ChessPosition(x + 2, y), null));
                myHash.add(new ChessMove(position, new ChessPosition(x + 1, y), null));
            }
        }
        if (myColor == ChessGame.TeamColor.BLACK && x == 7) {
            if (board.getPiece(new ChessPosition(x - 2, y)) == null && board.getPiece(new ChessPosition(x -1, y)) == null) {
                myHash.add(new ChessMove(position, new ChessPosition(x - 2, y), null));
                myHash.add(new ChessMove(position, new ChessPosition(x - 1, y), null));
            }
        }
        return myHash;
    }

    private HashSet<ChessMove> rookMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        while (y < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y++; // Increment the loop variable
        }
        y = position.getColumn();

        while (y > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y--; // Increment the loop variable
        }
        y = position.getColumn();

        while (x < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x++; // Increment the loop variable
        }
        x = position.getColumn();

        while (x > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x--; // Increment the loop variable
        }
        x = position.getColumn();
        return myHash;
    }

    private HashSet<ChessMove> bishopMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        while (y < 8 && x < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y++; // Increment the loop variable
            x++;
        }
        y = position.getColumn();
        x = position.getRow();
        while (y > -1 && x > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y--; // Increment the loop variable
            x--;
        }
        y = position.getColumn();
        x = position.getRow();
        while (x < 8 && y > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x++; // Increment the loop variable
            y--;
        }
        y = position.getColumn();
        x = position.getRow();
        while (x > -1 && y < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x--; // Increment the loop variable
            y++;
        }
        y = position.getColumn();
        x = position.getRow();
        return myHash;
    }

    private HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        while (y < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y++; // Increment the loop variable
        }
        y = position.getColumn();
        while (y > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y--; // Increment the loop variable
        }
        y = position.getColumn();
        while (x < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x++; // Increment the loop variable
        }
        x = position.getRow();
        while (x > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x--; // Increment the loop variable
        }
        x = position.getRow();
        while (y < 8 && x < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y++; // Increment the loop variable
            x++;
        }
        y = position.getColumn();
        x = position.getRow();
        while (y > -1 && x > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            y--; // Increment the loop variable
            x--;
        }
        y = position.getColumn();
        x = position.getRow();
        while (x < 8 && y > -1) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x++; // Increment the loop variable
            y--;
        }
        y = position.getColumn();
        x = position.getRow();
        while (x > -1 && y < 8) {
            if (board.getPiece(new ChessPosition(x, y)) == null || board.getPiece(new ChessPosition(x, y)).getTeamColor() != myColor) {
                myHash.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
            x--; // Increment the loop variable
            y++;
        }
        y = position.getColumn();
        x = position.getRow();
        return myHash;
    }

    private HashSet<ChessMove> knightMoves(ChessBoard board, ChessPosition position) {
        HashSet<ChessMove> myHash = new HashSet<>();
        ChessGame.TeamColor myColor = board.getPiece(position).getTeamColor();
        int y = position.getColumn();
        int x = position.getRow();
        if (board.getPiece(new ChessPosition(x + 3, y-1)) == null || board.getPiece(new ChessPosition(x + 3, y)).getTeamColor() != myColor) {
            myHash.add(new ChessMove(position, new ChessPosition(x + 3, y), null));
        }



        return myHash;
    }



}