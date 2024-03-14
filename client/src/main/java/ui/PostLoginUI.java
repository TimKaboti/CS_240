package ui;

import Result.CreateGameResult;
import model.*;

import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class PostLoginUI {

    private Scanner newScanner;
    public String authToken;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var input = "";
        System.out.println("Welcome to the Chess main menu!\n");
        System.out.println("\n1. Help\n");
        System.out.println("\n2. Logout\n");
        System.out.println("\n3. Create Game\n");
        System.out.println("\n4. List Games\n");
        System.out.println("\n5. Join Games\n");
        System.out.println("\n6. Join Observer\n");
        System.out.println("\n\n Enter the number of your desired option.");


        while (!input.equals("2")) {
            String line = scanner.nextLine();
            if (line.equals("3")) {
                System.out.println("\nEnter Game name:");
                Scanner newScanner = new Scanner(System.in);
                String gameName;
                String password;
                // Keep asking for username until it's not empty or just whitespace
                do {
                    System.out.println("\nEnter Game name:");
                    gameName = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (gameName.isEmpty()) {
                        System.out.println("\nGame name field cannot be blank. Please enter a game name.");
                    }
                } while (gameName.isEmpty());

                CreateGameRecord create = new CreateGameRecord(gameName);
            }

            if (line.equals("4")) {
                System.out.println("\nList Games.");
                Scanner newScanner = new Scanner(System.in);
                // Keep asking for username until it's not empty or just whitespace

                // Here you can proceed with password input and further logic
                ListGameRecord register = new ListGameRecord(authToken);
            }
            if (line.equals("5")) {
                System.out.println("\nEnter your color:");
                Scanner newScanner = new Scanner(System.in);
                String color;
                String gameID;
                // Keep asking for username until it's not empty or just whitespace
                do {
                    System.out.println("\nEnter your color:");
                    color = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (color.isEmpty()) {
                        System.out.println("\nPlayer color field cannot be blank. Please enter your color.");
                    }
                } while (color.isEmpty());

                System.out.println("\nEnter the game ID:");

                do {
                    System.out.println("\nEnter the game ID:");
                    gameID = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (gameID.isEmpty()) {
                        System.out.println("\nGame ID field cannot be blank. Please enter a game ID.");
                    }
                } while (gameID.isEmpty());
                int id = parseInt(gameID);
                JoinGameRecord join = new JoinGameRecord(color, id);
            }

            if (line.equals("5")) {
                System.out.println("\nEnter the game ID:");
                Scanner newScanner = new Scanner(System.in);
                String gameID;
                // Keep asking for username until it's not empty or just whitespace
                do {
                    System.out.println("\nEnter the game ID:");
                    gameID = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (gameID.isEmpty()) {
                        System.out.println("\nGame ID field cannot be blank. Please enter a valid game ID.");
                    }
                } while (gameID.isEmpty());
                int id = parseInt(gameID);
                JoinGameRecord observe = new JoinGameRecord(null, id);

            }

            if(line.equals("1")){
                System.out.println("\nYou are given the following options:");
                System.out.println("\nIf you want to logout and return to the main menu, enter '2'.");
                System.out.println("\nIf you would like to create a game, enter '3'.");
                System.out.println("\nIf you would like a list of all games, enter '4'.");
                System.out.println("\nIf you would like to join a game, enter '5'.");
                System.out.println("\nIf you would like to watch a game, but not play, enter '6'.");
            }
        }

    }
}
