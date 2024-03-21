package service;

import Result.ClearResult;
import Result.ListGamesResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import model.ListGameRecord;

import java.util.List;

public class ListGamesService {
    public Object gameList(ListGameRecord token, GameDAO games, AuthDAO auth) throws DataAccessException {
        List<GameData> gameList = null;
        ListGamesResult listResult;
        String key = token.authToken();

        try {
            if (!auth.getAuth(key)) { // Check if authentication fails
                listResult = new ListGamesResult(null, "Error: unauthorized");
            } else {
                gameList = games.listGames();
                listResult = new ListGamesResult(gameList, null);
            }
        } catch (DataAccessException e) {
            listResult = new ListGamesResult(null, "Error: unauthorized");
        }

        return listResult;
    }
}

