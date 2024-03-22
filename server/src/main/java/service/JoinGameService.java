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
            return new JoinGameResult(null,"Error: join service: authToken failed");
        }

        try {
            if (gameMemory.isNotNull(record.gameID())) {
                if (record.playerColor() == null) {
                    joinResult = new JoinGameResult(gameMemory.getGame(record.gameID()).getBoard(),null);
                } else if (gameMemory.taken(record.playerColor(), record.gameID())) {
                    joinResult = new JoinGameResult(null,"Error: already taken");
                } else {
                    try {
                        gameMemory.joinGame(username, record.playerColor(), record.gameID());
                        joinResult = new JoinGameResult(gameMemory.getGame(record.gameID()).getBoard(),null);
                    } catch (DataAccessException e) {
                        joinResult = new JoinGameResult(null,"Error: join game: join with specified color: failed");
                    }
                }
            } else {
                joinResult = new JoinGameResult(null,"Error: bad request");
            }

            if (record.gameID() == null) {
                joinResult = new JoinGameResult(null, "Error: bad request");
            }
        } catch (DataAccessException e) {
            joinResult = new JoinGameResult(null,"Error: join service: join game: failed");
        }

        return joinResult;
    }
}
