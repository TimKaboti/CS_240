package service;
import java.util.Random;
import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.CreateGameRecord;
import model.GameData;

import java.util.UUID;

public class CreateGameService {

    public GameData newGame (CreateGameRecord gameName, MemoryGameDAO games){
        Random randNum = new Random();
        String name = gameName.gameName();
        Integer gameID = randNum.nextInt();
        GameData game = new GameData(gameID, null, null, name, new ChessGame());
        games.gameData.put(gameID, game);
        return game;
    }
}
