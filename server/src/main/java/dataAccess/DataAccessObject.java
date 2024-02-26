package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject implements DataAccessInterface{

    @Override
    public void clear() {
        userData.clear();
        authData.clear();
        gameData.clear();
    }

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
    public String authToken() {
        return null;
    }

    @Override
    public void getAuth() {

    }

    @Override
    public void deleteAuth() {

    }
}
