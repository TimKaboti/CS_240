package service;

import Result.JoinGameResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.JoinGameRecord;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, GameDAO gameMemory, String authToken, AuthDAO authData) throws DataAccessException {
        JoinGameResult joinResult;
        joinResult = new JoinGameResult("Error: description");
        String username = (authData.getUsername(authToken));
        if(gameMemory.isNull(record.gameID())) {
            if(record.playerColor() == null){joinResult = new JoinGameResult(null);}
            else if(gameMemory.taken(record.playerColor(), record.gameID())){ joinResult = new JoinGameResult("Error: already taken");}
            else{gameMemory.joinGame(username, record.playerColor(), record.gameID());
                joinResult = new JoinGameResult(null);}
        }else{ joinResult = new JoinGameResult("Error: bad request");}
        if(record.gameID() == null) { joinResult = new JoinGameResult("Error: bad request");}
        return joinResult;
    }
}
