package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MemoryDAO implements DataAccessInterface{

    public Map<String, UserData> userData = null;
    public Map<Integer, GameData> gameData = null;
    public Map<String, AuthData> authData = null;
    @Override
    public void clear() {
        userData.clear();
        authData.clear();
        gameData.clear();
    }

    /** find out which methods should throw the dataAccessException **/

    @Override
    public void createUser(String username, String password, String email) {
        userData.put(username, new UserData(username,password,email));
    }

    @Override
    public UserData getUser(String username) {
        if (userData.get(username) == null) {
            return null;
        } else {
            return userData.get(username);
        }
    }
    @Override
    public void createGame() {
        new ChessGame();
    }

    @Override
    public ChessGame getGame(Integer ID) {
        if (gameData.get(ID) == null){
            return null;
        } else {
            return gameData.get(ID).getGame();
        }
    }

    @Override
    public List<ChessGame> listGames() {
        List<ChessGame> list = new ArrayList<>();
        for (GameData value: gameData.values()) {
            list.add(value.getGame());
        }
        return list;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public String CreateAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        authData.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String token) {
        return authData.get(token);
    }

    @Override
    public void deleteAuth(String token) {
        authData.remove(token);

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) {
        if (gameData.get(gameID) != null) {
            GameData game = gameData.get(gameID);
            game.renameUser(color, username);
        }
    }

}
