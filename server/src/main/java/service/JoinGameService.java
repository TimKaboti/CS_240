package service;

import Result.JoinGameResult;
import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.JoinGameRecord;

import java.util.Objects;

public class JoinGameService {

    public JoinGameResult joinGame(JoinGameRecord record, GameDAO gameMemory, String authToken, AuthDAO authData) throws DataAccessException {
        JoinGameResult joinResult;
        String username;
        String color =String.valueOf(ChessGame.TeamColor.valueOf(record.playerColor()));
        String desiredColor= color.toLowerCase();
        try {
            username = authData.getUsername(authToken);
        } catch (DataAccessException e) {
            return new JoinGameResult(null,"Error: join service: authToken failed");
        }
//        if(Objects.equals(username, blackPlayer)){desiredColor = "black";}
//        if(Objects.equals(username, whitePlayer)){desiredColor = "white";}
        try {
            String blackPlayer = gameMemory.blackPlayerName(record.gameID());
            String whitePlayer = gameMemory.whitePlayerName(record.gameID());
            if (gameMemory.isNotNull(record.gameID())) {
                if (record.playerColor() == null) {
                    joinResult = new JoinGameResult(gameMemory.getGame(record.gameID()).getBoard(),null);
                } else if (gameMemory.taken(record.playerColor(), record.gameID())) {
                    if(desiredColor.equals("black") && Objects.equals(username, blackPlayer)){
                        gameMemory.joinGame(username, record.playerColor(), record.gameID());
                    }
                    else if(desiredColor.equals("white") && Objects.equals(username, whitePlayer)){
                        gameMemory.joinGame(username, record.playerColor(), record.gameID());
                    }
                    else{joinResult=new JoinGameResult(null, "Error: already taken");}
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
