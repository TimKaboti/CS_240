package service;

import Result.ListGamesResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import model.ListGameRecord;

import java.util.List;

public class ListGamesService {
    public Object gameList (ListGameRecord token, GameDAO games, AuthDAO auth) throws DataAccessException {
        List<GameData> gameList = null;
        ListGamesResult listResult = new ListGamesResult(gameList , "Error: unauthorized" );
        String key = token.authToken();
        if (auth.getAuth(key)){
            gameList = games.listGames();
            listResult = new ListGamesResult( gameList, null);
        }

        return listResult;
    }
}
