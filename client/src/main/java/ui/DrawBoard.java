package ui;

import chess.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static java.lang.System.out;
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
    drawWhiteGrid(out);
    drawHeader(out);

    drawDivider(out);

    drawReverseHeader(out);
    drawBlackGrid(out);
    drawReverseHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
    System.out.println("\n");

  }

  private static void drawReverseHeader(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_BLACK);
    out.print("    h  g  f  e  d  c  b  a    ");
    out.print(SET_BG_COLOR_WHITE);
    out.print("\n");
  }

  private static void drawHeader(PrintStream out) {
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
  private static void drawBlackGrid(PrintStream out) {
    for (int i=1; i < 9; i++) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(" " + i + " ");
      for (int j=8; j > 0; j--) {
        if (i % 2 != 0) {
          if (j % 2 != 0) {
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
          } else if (j % 2 == 0) {
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
          }
        }
        if (i % 2 == 0) {
          if (j % 2 == 0) {
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

          } else if (j % 2 != 0) {
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
//                        out.print(EMPTY);
          }
        }
      }
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + i + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
  }


  public static void drawHighlightGrid(ChessPosition myPosition) {
    drawReverseHeader(out);
    Collection<ChessMove> moves = new ChessPiece(BLACK,board.getPiece(myPosition).getPieceType()).pieceMoves(board, myPosition);
    HashSet<ChessPosition> validMoves = new HashSet<ChessPosition>();
      for (ChessMove move : moves) {
      validMoves.add(move.getEndPosition());
    }
    for (int i=1; i < 9; i++) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + i + " ");
      for (int j=8; j > 0 ; j--) {
        if (i % 2 != 0) {
          if (j % 2 != 0) {
            ChessPosition position=new ChessPosition(i, j);
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
              out.print(SET_BG_COLOR_BLACK);
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
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
            }
          }
        }
        if (i % 2 == 0) {
          if (j % 2 == 0) {
            ChessPosition position=new ChessPosition(i, j);
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
              out.print(SET_BG_COLOR_BLACK);
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
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
            }
          }
        }
      }
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + i + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
    drawReverseHeader(out);
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  /**
   * draws the Chessboard from the P.O.V. of the white player.
   *
   * @param out
   */
  private static void drawWhiteGrid(PrintStream out) {
    for (int i=8; i > 0; i--) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(" " + i + " ");
      for (int j=1; j < 9; j++) {
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
      out.print(" " + i + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");

    }
  }


  public static void drawReversedHighlightGrid(ChessPosition myPosition) {
    Collection<ChessMove> moves = new ChessPiece(WHITE,board.getPiece(myPosition).getPieceType()).pieceMoves(board, myPosition);
    HashSet<ChessPosition> validMoves = new HashSet<ChessPosition>();
    drawHeader(out);
    for (ChessMove move : moves) {
      validMoves.add(move.getEndPosition());
    }
    for (int i=8; i > 0; i--) {
      out.print(SET_BG_COLOR_LIGHT_GREY);
      out.print(SET_TEXT_COLOR_BLACK);
      out.print(" " + i + " ");
      for (int j=1; j < 9; j++) {
        if (i % 2 == 0) {
          if (j % 2 != 0) {
            ChessPosition position=new ChessPosition(i, j);
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
        if (i % 2 != 0) {
          if (j % 2 == 0) {
            ChessPosition position=new ChessPosition(i, j);
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
            if (myPosition.equals(position)) {
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
            else if (validMoves.contains(position)) {
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
      out.print(" " + i + " ");
      out.print(SET_BG_COLOR_WHITE);
      out.print("\n");
    }
    drawHeader(out);
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public static void drawWhitePlayer() {
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    drawHeader(out);
    drawWhiteGrid(out);
    drawHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public static void drawBlackPlayer() {
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    drawReverseHeader(out);
    drawBlackGrid(out);
    drawReverseHeader(out);

    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  public static void drawBlackMoves(ChessPosition myPosition){
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    drawReverseHeader(out);
    drawHighlightGrid(myPosition);
    drawReverseHeader(out);
    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
    System.out.println("\n");
  }

  public static void drawWhiteMoves(ChessPosition myPosition){
    var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);
    drawHeader(out);
    drawReversedHighlightGrid(myPosition);
    drawHeader(out);
    out.print(RESET_BG_COLOR);
    out.print(SET_TEXT_COLOR_WHITE);
    System.out.println("\n");
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

}
