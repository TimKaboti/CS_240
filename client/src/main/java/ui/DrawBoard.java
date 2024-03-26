package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class DrawBoard {

  private static ChessBoard board=null;

  public DrawBoard(ChessBoard board) {
    this.board=board;
  }

  private static final int BOARD_HEIGHT=8;
  private static final int BOARD_WIDTH=8;
  private static final int SQUARE_WIDTH=3;
  private static final String EMPTY="   ";
  private static final String K=" K ";
  private static final String Q=" Q ";
  private static final String B=" B ";
  private static final String N=" N ";
  private static final String R=" R ";
  private static final String P=" P ";


  public static void draw() {
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    board.resetBoard();
    out.print(ERASE_SCREEN);

    drawHeader(out);
    drawReverseGrid(out);
    drawHeader(out);

    drawDivider(out);

    drawReverseHeader(out);
    drawGrid(out);
    drawReverseHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);

  }

  private static void drawHeader(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print("    h  g  f  e  d  c  b  a    ");
    out.print(SET_BG_COLOR_WHITE);
    out.print("\n");
  }

  private static void drawReverseHeader(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print("    a  b  c  d  e  f  g  h    ");
    out.print(SET_BG_COLOR_WHITE);
    out.print("\n");
  }

  private static void drawDivider(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    for (int i=0; i < 10; i++) {
      out.print(EMPTY);
    }
    out.print(SET_BG_COLOR_WHITE);
    out.print("\n");
  }

  /**
   * draws the Chessboard from the P.O.V. of the black player.
   *
   * @param out
   */
  private static void drawGrid(PrintStream out) {
    for (int i=1; i < 9; i++) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      int t=sideCounter(i);
      out.print(" " + t + " ");
      for (int j=1; j < 9; j++) {
        if (i % 2 != 0) {
          if (j % 2 != 0) {
            out.print(SET_BG_COLOR_WHITE);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
          } else if (j % 2 == 0) {
            out.print(SET_BG_COLOR_BLACK);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
          }
        }
        if (i % 2 == 0) {
          if (j % 2 == 0) {
            out.print(SET_BG_COLOR_WHITE);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }

          } else if (j % 2 != 0) {
            out.print(SET_BG_COLOR_BLACK);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
//                        out.print(EMPTY);
          }
        }
      }
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + t + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
  }


  private static void drawHighlightGrid(PrintStream out, ChessPosition myPosition) {
    Collection<ChessMove> moves = new ChessPiece(BLACK,board.getPiece(myPosition).getPieceType()).pieceMoves(board, myPosition);
    Collection<ChessPosition> validMoves=null;
    for (ChessMove move : moves) {
      validMoves.add(move.getEndPosition());
    }
    for (int i=1; i < 9; i++) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      int t=sideCounter(i);
      out.print(" " + t + " ");
      for (int j=1; j < 9; j++) {
        if (i % 2 != 0) {
          if (j % 2 != 0) {
            ChessPosition position=new ChessPosition(i, j);
            assert validMoves != null;
            if (myPosition == position) {
              out.print(SET_BG_COLOR_YELLOW);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            }
            if (validMoves.contains(position)) {
              out.print(SET_BG_COLOR_GREEN);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_MAGENTA);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            } else {
              out.print(SET_BG_COLOR_WHITE);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_RED);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLUE);
                  out.print(checkPiece(piece));
                }
              }
            }
          } else if (j % 2 == 0) {
            ChessPosition position=new ChessPosition(i, j);
            assert validMoves != null;
            if (myPosition == position) {
              out.print(SET_BG_COLOR_YELLOW);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            }
            if (validMoves.contains(position)) {
              out.print(SET_BG_COLOR_DARK_GREEN);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_MAGENTA);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            } else {
              out.print(SET_BG_COLOR_BLACK);
              ChessPiece piece=board.getPiece(new ChessPosition(i, j));
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_RED);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLUE);
                  out.print(checkPiece(piece));
                }
              }
            }
          }
        }
        if (i % 2 == 0) {
          if (j % 2 == 0) {
            ChessPosition position=new ChessPosition(i, j);
            assert validMoves != null;
            if (myPosition == position) {
              out.print(SET_BG_COLOR_YELLOW);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            }
            if (validMoves.contains(position)) {
              out.print(SET_BG_COLOR_GREEN);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_MAGENTA);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            } else {
              out.print(SET_BG_COLOR_WHITE);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_RED);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLUE);
                  out.print(checkPiece(piece));
                }
              }
            }

          } else if (j % 2 != 0) {
            ChessPosition position=new ChessPosition(i, j);
            assert validMoves != null;
            if (myPosition == position) {
              out.print(SET_BG_COLOR_YELLOW);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            }
            if (validMoves.contains(position)) {
              out.print(SET_BG_COLOR_DARK_GREEN);
              ChessPiece piece=board.getPiece(position);
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_MAGENTA);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLACK);
                  out.print(checkPiece(piece));
                }
              }
            } else {
              out.print(SET_BG_COLOR_BLACK);
              ChessPiece piece=board.getPiece(new ChessPosition(i, j));
              if (piece == null) {
                out.print(EMPTY);
              } else {
                if (BLACK.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_RED);
                  out.print(checkPiece(piece));
                } else if (WHITE.equals(piece.getTeamColor())) {
                  out.print(SET_TEXT_COLOR_BLUE);
                  out.print(checkPiece(piece));
                }
              }
            }
          }
        }
      }
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + t + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
  }

  /**
   * draws the Chessboard from the P.O.V. of the white player.
   *
   * @param out
   */
  private static void drawReverseGrid(PrintStream out) {
    for (int i=8; i > 0; i--) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      int t=sideCounter(i);
      out.print(" " + t + " ");
      for (int j=8; j > 0; j--) {
        if (i % 2 == 0) {
          if (j % 2 != 0) {
            out.print(SET_BG_COLOR_WHITE);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
            /**check the chessboard at this i,j-1 coordinate for a piece
             * if coord is null, print EMPTY. if not call a method that uses a switch statement
             * and returns the String above that corresponds with the appropriate piece type. **/
          } else if (j % 2 == 0) {
            out.print(SET_BG_COLOR_BLACK);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
//                        out.print(EMPTY);
          }
        }
        if (i % 2 != 0) {
          if (j % 2 == 0) {
            out.print(SET_BG_COLOR_WHITE);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
            /** do the same piece coordinate comparison here.**/
//                        out.print(EMPTY);
          } else if (j % 2 != 0) {
            out.print(SET_BG_COLOR_BLACK);
            ChessPiece piece=board.getPiece(new ChessPosition(i, j));
            if (piece == null) {
              out.print(EMPTY);
            } else {
              if (BLACK.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(checkPiece(piece));
              } else if (WHITE.equals(piece.getTeamColor())) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(checkPiece(piece));
              }
            }
//                        out.print(EMPTY);
          }
        }
      }
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + t + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
  }

  public static void drawWhitePlayer() {
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    board.resetBoard();
    out.print(ERASE_SCREEN);
    drawHeader(out);
    drawReverseGrid(out);
    drawHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public static void drawBlackPlayer() {
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    board.resetBoard();
    out.print(ERASE_SCREEN);
    drawReverseHeader(out);
    drawGrid(out);
    drawReverseHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private static String checkPiece(ChessPiece piece) {
    String type=EMPTY;
    switch (piece.getPieceType()) {
      case KING:
        type=K;
        return type;
      case QUEEN:
        type=Q;
        return type;
      case BISHOP:
        type=B;
        return type;
      case KNIGHT:
        type=N;
        return type;
      case ROOK:
        type=R;
        return type;
      case PAWN:
        type=P;
        return type;
    }
    return type;
  }

  private static String checkPieceWithColor(ChessPiece piece) {
    String type=EMPTY;
    if (piece.getTeamColor() == BLACK) {
      switch (piece.getPieceType()) {
        case KING:
          type=K;
          return BLACK_KING;
        case QUEEN:
          type=Q;
          return BLACK_QUEEN;
        case BISHOP:
          type=B;
          return BLACK_BISHOP;
        case KNIGHT:
          type=N;
          return BLACK_KNIGHT;
        case ROOK:
          type=R;
          return BLACK_ROOK;
        case PAWN:
          type=P;
          return BLACK_PAWN;
      }
    }

    if (piece.getTeamColor() == WHITE) {
      switch (piece.getPieceType()) {
        case KING:
          type=K;
          return WHITE_KING;
        case QUEEN:
          type=Q;
          return WHITE_QUEEN;
        case BISHOP:
          type=B;
          return WHITE_BISHOP;
        case KNIGHT:
          type=N;
          return WHITE_KNIGHT;
        case ROOK:
          type=R;
          return WHITE_ROOK;
        case PAWN:
          type=P;
          return WHITE_PAWN;
      }
    }
    return type;
  }

  private static int sideCounter(int num) {
    int row=0;
    switch (num) {
      case 1:
        row=8;
        return row;
      case 2:
        row=7;
        return row;
      case 3:
        row=6;
        return row;
      case 4:
        row=5;
        return row;
      case 5:
        row=4;
        return row;
      case 6:
        row=3;
        return row;
      case 7:
        row=2;
        return row;
      case 8:
        row=1;
        return row;
    }
    return row;
  }
}
