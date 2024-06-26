package ui;
import chess.ChessGame;
import com.google.gson.Gson;
import Result.JoinGameResult;
import Result.ListGamesResult;
import model.CreateGameRecord;
import model.GameData;
import model.JoinGameRecord;
import model.LogoutRecord;
import server.NotificationHandler;
import server.WebsocketCommunicator;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import java.util.List;
import java.util.Scanner;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.lang.Integer.parseInt;
import static org.glassfish.grizzly.Interceptor.RESET;

public class PostLoginUI implements NotificationHandler {

  PreLoginUI preMenu=new PreLoginUI();
  WebsocketCommunicator communicator;
  {
    try {
      communicator=new WebsocketCommunicator(this);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String authToken;
  String color;

  public void run(ServerFacade server, String authToken) throws ResponseException, Exception {
    Scanner scanner=new Scanner(System.in);
    var input="";
    System.out.println("Welcome to the Chess game options menu!");
    options();


    while (true) {
      String line=scanner.nextLine();
      if (line.equals("3")) {
        Scanner newScanner=new Scanner(System.in);
        String gameName;
        do {
          System.out.println("\nEnter Game name:");
          gameName=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (gameName.isEmpty()) {
            System.out.println("\nGame name field cannot be blank. Please enter a game name.");
          }
        } while (gameName.isEmpty());

        CreateGameRecord create=new CreateGameRecord(gameName);
        try {
          int id=server.facadeCreate(create).gameID();
          String num=String.valueOf(id);
          System.out.println("Game successfully created");
          options();
        } catch (ResponseException e) {
          System.out.println("\nTrouble creating game, please try again.");
        }
      } else if (line.equals("4")) {
        System.out.println("\nGames:");
        try {
          ListGamesResult result=(server.facadeList());
          System.out.println(listToString(result.games()));
          options();
        } catch (ResponseException e) {
          System.out.println("\nTrouble listing games, please try again.");
          ;
        }
      } else if (line.equals("5")) {
        Scanner newScanner=new Scanner(System.in);
        String gameID;
        // Keep asking for username until it's not empty or just whitespace
        do {
          System.out.println("\nEnter your color:");
          color=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (color.isEmpty() || color.matches("\\d+")) {
            System.out.println("\nPlayer color field cannot be blank or a number. Please enter your color.");
          }
        } while (color.isEmpty());
          color = color.toLowerCase();

        do {
          System.out.println("\nEnter the game ID#:");
          gameID=newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
          if (gameID.isEmpty() || gameID.matches("[a-zA-Z]+")) {
            System.out.println("\nGame ID field cannot be blank or a string. Please enter a game ID#.");
          }
        } while (gameID.isEmpty());
        ListGamesResult result = (server.facadeList());
        List<GameData> games = result.games();
        int index=parseInt(gameID);
        GameData game = games.get(index - 1);
        int id=game.getGameID();
        JoinGameRecord join = new JoinGameRecord(color, id);
        GamePlayUI play = new GamePlayUI(this,authToken,color,game.getGame(), id, communicator);
        try {
          JoinGameResult temp = server.facadeJoin(join);
          DrawBoard board=new DrawBoard(temp.board());
          if(color.equalsIgnoreCase("white")){
            String playerJoin = new Gson().toJson(new JoinPlayer(authToken, id, ChessGame.TeamColor.WHITE));
            communicator.send(playerJoin);
            board.drawWhitePlayer();
            System.out.println("\n");
            play.run(server, authToken);
            //            run the gameplay UI and send a websocket message maybe?
            String joinPlayer =  new Gson().toJson(new JoinPlayer(authToken, id, ChessGame.TeamColor.WHITE));
            communicator.send(joinPlayer);
          }
          if(color.equalsIgnoreCase("black")){
            String playerJoin = new Gson().toJson(new JoinPlayer(authToken, id, ChessGame.TeamColor.BLACK));
            communicator.send(playerJoin);
            board.drawBlackPlayer();
            System.out.println("\n");
            play.run(server, authToken);
            //            run the gameplay UI and send a websocket message maybe?
              String joinPlayer = new Gson().toJson(new JoinPlayer(authToken, id, ChessGame.TeamColor.BLACK));
              communicator.send(joinPlayer);
          }

        } catch (ResponseException e) {
          System.out.println("\nColor already taken.");
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
//        websocket message here.

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
        GamePlayUI play = new GamePlayUI(this,authToken,null,game.getGame(), id, communicator);
        try {
          DrawBoard board=new DrawBoard(server.facadeJoin(observe).board());
          board.drawWhitePlayer();
          String obbyJoin = new Gson().toJson(new JoinObserver(authToken, id));
          communicator.send(obbyJoin);
          play.run(server, authToken);
        } catch (ResponseException e) {
          System.out.println("\nTrouble observing game, please try again.");

        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        System.out.println("\n");
      } else if (line.equals("1")) {
        System.out.println("You are given the following options:");
        System.out.println("If you want to logout and return to the main menu, enter '2'.");
        System.out.println("If you would like to create a game, enter '3'.");
        System.out.println("If you would like a list of all games, enter '4'.");
        System.out.println("If you would like to join a game, enter '5'. You will be asked for a game ID.");
        System.out.println("To find the game ID, go to 'List Games'.");
        System.out.println("If you would like to watch a game, but not play, enter '6'.");
        System.out.println("\n");
        options();
      } else if (line.equals("2")) {
        break;
      } else {
        System.out.println("\nPlease enter a valid menu option by typing the number of the option you want.");
      }
    }

    LogoutRecord logout=new LogoutRecord(authToken);
    try {
      server.facadeLogout(logout);
    } catch (ResponseException e) {
      System.out.println("\nFailed to logout. Please try again.");
    }
    preMenu.run();
  }

  public String listToString(List<GameData> result) {
    String games="";
    for (int i=0; i < result.size(); i++) {
      String num=Integer.toString(i + 1);
      games+=(num + ". " + result.get(i).getGameName() + "\n");
    }
    return games;
  }

  public void options() {
    System.out.println("1. Help");
    System.out.println("2. Logout");
    System.out.println("3. Create Game");
    System.out.println("4. List Games");
    System.out.println("5. Join Games");
    System.out.println("6. Join Observer");
    System.out.println("\n Enter the number of your desired action.");
  }

  public void notify(Notification notification) {
    System.out.println(RED + notification.getMessage());
    printPrompt();
  }

  private void printPrompt() {
    System.out.print("\n" + RESET + ">>> " + GREEN);
  }

  @Override
  public void notify(String message) {
    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
    switch(msg.getServerMessageType()) {
      case LOAD_GAME:
        LoadGame load=new Gson().fromJson(message.toString(), LoadGame.class);
        ChessGame temp=load.getGame();
        if (color.equalsIgnoreCase("black")) {
          DrawBoard board=new DrawBoard(temp.getBoard());
          board.drawBlackPlayer();
        } else if (color.equalsIgnoreCase("white")) {
          DrawBoard board=new DrawBoard(temp.getBoard());
          board.drawWhitePlayer();
        } else {
          DrawBoard board=new DrawBoard(temp.getBoard());
          board.draw();
        }
        break;
      case NOTIFICATION:
        Notification notification=new Gson().fromJson(message.toString(), Notification.class);
        System.out.println(notification.getMessage());
        break;
      case ERROR:
        Error error=new Gson().fromJson(message.toString(), Error.class);
        System.out.println(error.getErrorMessge());
        break;
    }
  }
}
