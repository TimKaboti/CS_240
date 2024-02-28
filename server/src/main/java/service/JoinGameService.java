package service;

import Result.JoinGameResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.JoinGameRecord;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, MemoryGameDAO gameMemory, String authToken, MemoryAuthDAO authData){
        String username = String.valueOf(authData.getAuth(authToken));
        gameMemory.joinGame(username, record.color(), record.ID());
        return null;

        /**all your services should return a result record of the info they put together. none of them have this yet. go back through and add it in. **/
    }
}
