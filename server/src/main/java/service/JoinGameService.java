package service;

import Result.JoinGameResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.JoinGameRecord;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, MemoryGameDAO gameMemory, String authToken, MemoryAuthDAO authData){
        JoinGameResult joinResult = new JoinGameResult("Error: description");
        String username = (authData.getAuth(authToken).getUsername());
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
