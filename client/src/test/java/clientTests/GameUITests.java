package clientTests;

import Result.*;
import chess.ChessPosition;
import dataAccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.DrawBoard;
import ui.GamePlayUI;
import ui.ResponseException;
import ui.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import ui.GamePlayUI;

public class GameUITests {


  @Test
    public void drawBoardBothPlayers(){
    ChessGame game = new ChessGame();
    ChessBoard board = game.getBoard();
    board.resetBoard();
    DrawBoard grid = new DrawBoard(board);
    grid.draw();
  }

@Test
  public void highlightGridTest(){
    ChessGame game = new ChessGame();
    ChessBoard board = game.getBoard();
    board.resetBoard();
    ChessPosition pos = new ChessPosition(1,1);
    DrawBoard grid = new DrawBoard(board);
    grid.drawBlackMoves(pos);

  }

  @Test
  public void reversedHighlightGridTest(){
    ChessGame game = new ChessGame();
    ChessBoard board = game.getBoard();
    board.resetBoard();
    ChessPosition pos =GamePlayUI.coordConvert("f7");
//    ChessPosition pos = new ChessPosition(1,1);
    DrawBoard grid = new DrawBoard(board);
    grid.drawWhiteMoves(pos);

  }


}


