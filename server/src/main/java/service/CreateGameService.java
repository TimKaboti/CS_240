package service;
import java.util.Random;

import Result.CreateGameResult;
import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.CreateGameRecord;
import model.GameData;

import java.util.UUID;

public class CreateGameService {

    public Object newGame (CreateGameRecord gameName, MemoryGameDAO games){
        CreateGameResult  newGameResult;
        Random randNum = new Random();
        String name = gameName.gameName();
        Integer gameID = randNum.nextInt();
        GameData game = new GameData(gameID, null, null, name, new ChessGame());
        if(!games.gameData.containsValue(game)) {
            games.gameData.put(gameID, game);
            newGameResult = new CreateGameResult(gameID, null);
        } else { newGameResult = new CreateGameResult(null, "Error: bad request");}
        return newGameResult;
    }
}
