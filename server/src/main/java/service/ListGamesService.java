package service;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.ListGameRecord;

import java.util.List;

public class ListGamesService {

    public List<ChessGame> gameList (ListGameRecord token, MemoryGameDAO games, MemoryAuthDAO auth){
        List<ChessGame> gameList = null;
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            gameList = games.listGames();
        }

        return gameList;
    }
}
