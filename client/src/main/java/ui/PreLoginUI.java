package ui;

import model.LoginRecord;
import model.RegisterRecord;

import java.util.Scanner;

public class PreLoginUI {

    PostLoginUI postMenu = new PostLoginUI();
    ServerFacade facade = new ServerFacade("http://localhost:8080");
    private Scanner newScanner;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var input = "";
        System.out.println("Welcome to the Chess main menu!\n");
        System.out.println("\n1. Help\n");
        System.out.println("\n2. Login\n");
        System.out.println("\n3. Register\n");
        System.out.println("\n4. Quit\n");
        System.out.println("\n\n Enter the number of your desired option.");

        while (!input.equals("4")) {
            String line = scanner.nextLine();
            if (line.equals("2")) {
                System.out.println("\nEnter your username:");
                Scanner newScanner = new Scanner(System.in);
                String username;
                String password;
                // Keep asking for username until it's not empty or just whitespace
                do {
                    System.out.println("\nEnter your username:");
                    username = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (username.isEmpty()) {
                        System.out.println("\nUsername field cannot be blank. Please enter your username.");
                    }
                } while (username.isEmpty());

                System.out.println("\nEnter your password:");
                // Here you can proceed with password input and further logic

                do {
                    System.out.println("\nEnter your password:");
                    password = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (password.isEmpty()) {
                        System.out.println("\nUsername field cannot be blank. Please enter your password.");
                    }
                } while (password.isEmpty());

                System.out.println("\nEnter your password:");
                // Here you can proceed with password input and further logic
                LoginRecord login = new LoginRecord(username, password);
                try{facade.facadeLogin(login);
                    postMenu.run(facade);} catch(ResponseException e){
                    System.out.println("\nTrouble logging in with this info. Please try again.");
                }

            }

            else if (line.equals("3")) {
                System.out.println("\nEnter a username:");
                Scanner newScanner = new Scanner(System.in);
                String username;
                String password;
                String email;
                // Keep asking for username until it's not empty or just whitespace
                do {
                    System.out.println("\nEnter your username:");
                    username = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (username.isEmpty()) {
                        System.out.println("\nUsername field cannot be blank. Please enter a new username.");
                    }
                } while (username.isEmpty());

                System.out.println("\nEnter your password:");
                // Here you can proceed with password input and further logic

                do {
                    System.out.println("\nEnter your password:");
                    password = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (password.isEmpty()) {
                        System.out.println("\nPassword field cannot be blank. Please enter a new password.");
                    }
                } while (password.isEmpty());

                System.out.println("\nEnter your email.");

                do {
                    System.out.println("\nEnter your email:");
                    email = newScanner.nextLine().trim(); // trim to remove leading and trailing whitespace
                    if (email.isEmpty()) {
                        System.out.println("\nEmail field cannot be blank. Please enter your email.");
                    }
                } while (email.isEmpty());

                // Here you can proceed with password input and further logic
                RegisterRecord register = new RegisterRecord(username, password, email);
                try{facade.facadeRegister(register);
                    postMenu.run(facade);} catch(ResponseException e){
                    System.out.println("\nTrouble registering this account. Please try again.");}

            }

            else if(line.equals("1")){
                System.out.println("\nYou are given the following options:");
                System.out.println("\nIf you want to login to your account and be able to access Chess games, enter '2'.");
                System.out.println("\nIf you would like to register an account, enter '3'.");
                System.out.println("\nTo leave this menu, enter '4'.");
            }
            else{
                System.out.println("\nPlease enter a valid menu option by typing the number of the option you want.");
            }
        }


    }
}

