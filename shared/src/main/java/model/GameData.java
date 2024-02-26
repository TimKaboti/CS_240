package model;

import chess.ChessGame;

public class GameData {

    int gameID = 0;
    String whiteUsername = "";
    String blackUsername = "";
    String gameName = "";
    ChessGame game = new ChessGame();

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
}
