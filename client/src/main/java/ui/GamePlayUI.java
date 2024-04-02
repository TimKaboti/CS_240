package ui;

import Result.JoinGameResult;
import Result.ListGamesResult;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.CreateGameRecord;
import model.GameData;
import model.JoinGameRecord;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class GamePlayUI {


  public ChessGame game=null;
  PostLoginUI postMenu;

  public String authToken;
  public String playerColor;


  public GamePlayUI(PostLoginUI postMenu, String authToken, String playerColor, ChessGame game) {
    this.postMenu=postMenu;
    this.authToken=authToken;
    this.playerColor=playerColor;
    this.game=game;
  }


  public void run(ServerFacade server, String authToken, ChessGame chessGame) throws ResponseException {
    DrawBoard board=new DrawBoard(chessGame.getBoard());
    Scanner scanner=new Scanner(System.in);
    var input="";
    System.out.println("Welcome to the Gameplay menu.");
    options();


    while (true) {
      String line=scanner.nextLine();
      if (line.equals("4")) {
        Scanner newScanner=new Scanner(System.in);
        String startPos;
        String endPos;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("Enter Chosen Piece's starting position:");
          startPos=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (startPos.isEmpty()) {
            System.out.println("field cannot be blank. Please enter a start position.");
          }
        } while (startPos.isEmpty());

        do {
          System.out.println("Enter the desired end position:");
          endPos=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (endPos.isEmpty()) {
            System.out.println("field cannot be blank. Please enter an end position.");
          }
        } while (endPos.isEmpty());

        ChessPosition start=coordConvert(startPos);
        ChessPosition end=coordConvert(endPos);
//                i will likely need to look into how to properly do this.
        ChessMove move=new ChessMove(start, end, null);
//        not sure, but I may need to call the serverFacade with the make move, or 'update game' method
//        also might need to have a websocket message.


      } else if (line.equals("3")) {
        Scanner newScanner=new Scanner(System.in);
        if (playerColor.equalsIgnoreCase("black")) {
          board.drawBlackPlayer();
          options();
        }
        if (playerColor.equalsIgnoreCase("white")) {
          board.drawBlackPlayer();
          options();
        }

      } else if (line.equals("5")) {
        Scanner newScanner=new Scanner(System.in);
        String position;
        String gameID;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("\nEnter the start positon of the piece:");
          position=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (position.isEmpty() || position.matches("\\d+")) {
            System.out.println("\nthis field cannot be blank. Please enter your color.");
          }
        } while (position.isEmpty());

        ChessPosition start = coordConvert(position);
        if(playerColor.equalsIgnoreCase("white")){
          board.drawReversedHighlightGrid(start);
        }
        if(playerColor.equalsIgnoreCase("black")){
          board.drawHighlightGrid(start);
        }
//        websocket messages for these

      } else if (line.equals("6")) {
        Scanner newScanner=new Scanner(System.in);
//        this is for resigning. need to implement websocket.

        System.out.println("\n");

      } else if (line.equals("1")) {
        System.out.println("You are given the following options:");
        System.out.println("If you would like to leave the game, enter '2'.");
        System.out.println("If you want to ensure the Chess Board is up to date, enter '3'.");
        System.out.println("If you would like to make a move, enter '4'.");
        System.out.println("If you would like to see all valid moves a piece can make, enter '5'.");
        System.out.println("If you would like to resign, enter '6'. Be aware; if you resign you lose the match.");
        System.out.println("\n");
        options();
      } else if (line.equals("2")) {
        System.out.println("Leaving Game.");
//        websocket message needed.
        break;
      } else {
        System.out.println("\nPlease enter a valid menu option by typing the number of the option you want.");
      }
    }
    postMenu.run(server, authToken);
  }


  public void options() {
    System.out.println("1. Help");
    System.out.println("2. Leave Game");
    System.out.println("3. Redraw Chess Board");
    System.out.println("4. Make Move");
    System.out.println("5. Highlight Legal Moves");
    System.out.println("6. Resign");
    System.out.println("\n Enter the number of your desired action.");
  }

  public static ChessPosition coordConvert(String location) {
    int col=0;
    int row=0;
    char letter = location.charAt(0);
    col = letter - 'a' + 1;
    char num = location.charAt(1);
    row = num - '1' + 1;
    return new ChessPosition(row, col);
  }

//  public void notify(Notification notification) {
//    System.out.println(RED + notification.message());
//    printPrompt();
//  }
//
//  private void printPrompt() {
//    System.out.print("\n" + RESET + ">>> " + GREEN);
//  }

}
