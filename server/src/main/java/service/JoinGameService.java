package service;

import Result.JoinGameResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.JoinGameRecord;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, GameDAO gameMemory, String authToken, AuthDAO authData) {
        JoinGameResult joinResult;
        String username;

        try {
            username = authData.getUsername(authToken);
        } catch (DataAccessException e) {
            return new JoinGameResult("Error: description");
        }

        try {
            if (gameMemory.isNull(record.gameID())) {
                if (record.playerColor() == null) {
                    joinResult = new JoinGameResult(null);
                } else if (gameMemory.taken(record.playerColor(), record.gameID())) {
                    joinResult = new JoinGameResult("Error: already taken");
                } else {
                    try {
                        gameMemory.joinGame(username, record.playerColor(), record.gameID());
                        joinResult = new JoinGameResult(null);
                    } catch (DataAccessException e) {
                        joinResult = new JoinGameResult("Error: description");
                    }
                }
            } else {
                joinResult = new JoinGameResult("Error: bad request");
            }

            if (record.gameID() == null) {
                joinResult = new JoinGameResult("Error: bad request");
            }
        } catch (DataAccessException e) {
            joinResult = new JoinGameResult("Error: description");
        }

        return joinResult;
    }
}
