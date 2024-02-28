package service;

import Result.ListGamesResult;
import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import model.ListGameRecord;

import java.util.List;

public class ListGamesService {

    public Object gameList (ListGameRecord token, MemoryGameDAO games, MemoryAuthDAO auth){
        List<GameData> gameList = null;
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            gameList = games.listGames();
            ListGamesResult listResult = new ListGamesResult("games", gameList);
        }

        return gameList;
    }
}
