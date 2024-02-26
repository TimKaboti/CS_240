package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public interface DataAccessInterface {

    public Map<String, UserData> userData = null;
    public Map<Integer, GameData> gameData = null;
    public Map<String, AuthData> authData = null;

    public void clear();
    public void createUser(String username, String password, String email);
    public UserData getUser(String username);
    public void createGame();
    public ChessGame getGame(Integer ID);
    public List<ChessGame> listGames();
    public void updateGame();
    public String authToken();
    public void getAuth();
    public void deleteAuth();
}