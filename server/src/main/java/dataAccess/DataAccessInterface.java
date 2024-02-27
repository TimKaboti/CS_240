package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.*;

public interface DataAccessInterface {

    public void clear();
    public void createUser(String username, String password, String email) throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void createGame() throws DataAccessException;
    public ChessGame getGame(Integer ID) throws DataAccessException;
    public List<ChessGame> listGames() throws DataAccessException;
    public void updateGame() throws DataAccessException;
    public String CreateAuth(String username) throws DataAccessException;
    public AuthData getAuth(String token) throws DataAccessException;
    public void deleteAuth(String token) throws DataAccessException;
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException;
}