package service;

import Result.JoinGameResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.JoinGameRecord;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, MemoryGameDAO gameMemory, String authToken, MemoryAuthDAO authData){
        JoinGameResult joinResult;
        String username = String.valueOf(authData.getAuth(authToken));
        if(gameMemory.isNull(username, record.ID())) {
            gameMemory.joinGame(username, record.color(), record.ID());
            joinResult = new JoinGameResult(null);
        } else {
            joinResult = new JoinGameResult("Error: bad requesst");
        }
        return joinResult;
    }
}
