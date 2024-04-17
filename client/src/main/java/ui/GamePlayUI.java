package ui;

import Result.JoinGameResult;
import Result.ListGamesResult;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.CreateGameRecord;
import model.GameData;
import model.JoinGameRecord;
import server.NotificationHandler;
import server.WebsocketCommunicator;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class GamePlayUI implements NotificationHandler {


  public ChessGame game=null;
  PostLoginUI postMenu;
  WebsocketCommunicator communicator;
  public String authToken;
  public String playerColor;
  public int gameID;
  DrawBoard board;


  public GamePlayUI(PostLoginUI postMenu, String authToken, String playerColor, ChessGame game, int gameID, WebsocketCommunicator communicator) {
    this.postMenu=postMenu;
    this.authToken=authToken;
    this.playerColor=playerColor;
    this.game=game;
    this.gameID=gameID;
    this.communicator = communicator;
  }


  public void run(ServerFacade server, String authToken) throws ResponseException, Exception {
//    DrawBoard board=new DrawBoard(game.getBoard());
    Scanner scanner=new Scanner(System.in);
    System.out.println(SET_TEXT_COLOR_WHITE);
    System.out.println("Welcome to the Gameplay menu.");
    options();
    var input="";


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
        String tmpMove = new Gson().toJson(new MakeMove(authToken, gameID, move));
        communicator.send(tmpMove);
        options();

      } else if (line.equals("3")) {
        if(playerColor == null){
          board.draw();
          options();
        }
        if (playerColor.equalsIgnoreCase("black")) {
          board.drawBlackPlayer();
          options();
        }
        else if (playerColor.equalsIgnoreCase("white")) {
          board.drawWhitePlayer();
          options();
        } else{
          board.draw();
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
        options();

      } else if (line.equals("6")) {
        Scanner newScanner=new Scanner(System.in);
        String resign = new Gson().toJson(new Resign(authToken, gameID));
        communicator.send(resign);

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
        String leave = new Gson().toJson(new Leave(authToken, gameID));
        communicator.send(leave);
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
    col = letter -'a' + 1;
    char num = location.charAt(1);
    row = num - '1' + 1;
    return new ChessPosition(row, col);
  }

  @Override
  public void notify(String message) {
    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
    switch(msg.getServerMessageType()){
      case LOAD_GAME:
        System.out.println(message);
        LoadGame load = new Gson().fromJson(message, LoadGame.class);
        game = load.getGame();
        board=new DrawBoard(load.getGame().getBoard());
        if (playerColor.equalsIgnoreCase("black")) {
          board.drawBlackPlayer();
          options();
        }
        if (playerColor.equalsIgnoreCase("white")) {
          board.drawBlackPlayer();
          options();
        }
        load.notify();
        break;
      case NOTIFICATION:
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
        break;
      case ERROR:
        Error error = new Gson().fromJson(message, Error.class);
        System.out.println(error.getMessage());
        break;
    }

  }

}
