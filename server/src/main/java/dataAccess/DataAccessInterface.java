package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public interface DataAccessInterface {

    public void clear();
    public void createUser(String username, String password, String email);
    public UserData getUser(String username);
    public void createGame();
    public ChessGame getGame(Integer ID);
    public List<ChessGame> listGames();
    public void updateGame();
    public String CreateAuth(String username);
    public AuthData getAuth(String token);
    public void deleteAuth(String token);
    public void joinGame(String username, String color, Integer gameID);
}