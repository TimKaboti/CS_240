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
  PostLoginUI postMenu=new PostLoginUI();

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
    System.out.println("Welcome to the Chess game options menu!");
    options();


    while (true) {
      String line=scanner.nextLine();
      if (line.equals("4")) {
        Scanner newScanner=new Scanner(System.in);
        String startPos;
        String endPos;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("\nEnter Chosen Piece's starting position:");
          startPos=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (startPos.isEmpty()) {
            System.out.println("\nGame name field cannot be blank. Please enter a start position.");
          }
        } while (startPos.isEmpty());

        do {
          System.out.println("\nEnter the desired end position:");
          endPos=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (endPos.isEmpty()) {
            System.out.println("\nGame ID field cannot be blank. Please enter an end position.");
          }
        } while (endPos.isEmpty());

        ChessPosition start=coordConvert(startPos);
        ChessPosition end=coordConvert(endPos);
//                i will likely need to look into how to properly do this.
        ChessMove move=new ChessMove(start, end, null);
        CreateGameRecord create=new CreateGameRecord(startPos);
        try {
          int id=server.facadeCreate(create).gameID();
          String num=String.valueOf(id);
          System.out.println("Game successfully created");
          options();
        } catch (ResponseException e) {
          System.out.println("\nTrouble creating game, please try again.");
        }
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
        String color;
        String gameID;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("\nEnter your color:");
          color=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (color.isEmpty() || color.matches("\\d+")) {
            System.out.println("\nPlayer color field cannot be blank or a number. Please enter your color.");
          }
        } while (color.isEmpty());


        do {
          System.out.println("\nEnter the game ID#:");
          gameID=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (gameID.isEmpty() || gameID.matches("[a-zA-Z]+")) {
            System.out.println("\nGame ID field cannot be blank or a string. Please enter a game ID#.");
          }
        } while (gameID.isEmpty());
        ListGamesResult result=(server.facadeList());
        List<GameData> games=result.games();
        int index=parseInt(gameID);
        GameData game=games.get(index - 1);
        int id=game.getGameID();
        JoinGameRecord join=new JoinGameRecord(color, id);
        try {
          JoinGameResult temp=server.facadeJoin(join);
//          DrawBoard board=new DrawBoard(temp.board());
          board.draw();
//                    if (color.equals("black")) {
//                        board.drawBlackPlayer();
//                        System.out.println("\n");
//                    } else {
//                        board.drawWhitePlayer();
//                        System.out.println("\n");
//                    }
        } catch (ResponseException e) {
          System.out.println("\nColor already taken.");
        }
      } else if (line.equals("6")) {
        Scanner newScanner=new Scanner(System.in);
        String gameID;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("\nEnter the game ID#:");
          gameID=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (gameID.isEmpty() || gameID.matches("[a-zA-Z]+")) {
            System.out.println("\nGame ID field cannot be blank or a string. Please enter a valid game ID#.");
          }
        } while (gameID.isEmpty());
        ListGamesResult result=(server.facadeList());
        List<GameData> games=result.games();
        int index=parseInt(gameID);
        GameData game=games.get(index - 1);
        int id=game.getGameID();
        JoinGameRecord observe=new JoinGameRecord(null, id);
        //          DrawBoard board=new DrawBoard(server.facadeJoin(observe).board());
        board.draw();
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

  public ChessPosition coordConvert(String location) {
    int col=0;
    int row=0;
    String letter=location.substring(0, -1);
    String num=location.substring(-1);
    if (letter != null) {
      switch (letter) {
        case "a":
          col=1;
          break;
        case "b":
          col=2;
          break;
        case "c":
          col=3;
          break;
        case "d":
          col=4;
          break;
        case "e":
          col=5;
          break;
        case "f":
          col=6;
          break;
        case "g":
          col=7;
          break;
        case "h":
          col=8;
          break;
      }

      if (num != null) {
        switch (num) {
          case "1":
            row=1;
            break;
          case "2":
            row=2;
            break;
          case "3":
            row=3;
            break;
          case "4":
            row=4;
            break;
          case "5":
            row=5;
            break;
          case "6":
            row=6;
            break;
          case "7":
            row=7;
            break;
          case "8":
            row=8;
            break;
        }
      }
    }
    return new ChessPosition(row, col);
  }
}
