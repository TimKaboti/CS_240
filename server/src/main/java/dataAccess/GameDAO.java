package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public interface GameDAO {

    public void clear() throws DataAccessException;
    public void createGame() throws DataAccessException;
    public ChessGame getGame(Integer ID) throws DataAccessException;
    public List<GameData> listGames() throws DataAccessException;
    public void updateGame() throws DataAccessException;
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException;
    public boolean isNull (Integer gameID) throws DataAccessException;
    public boolean taken (String color, Integer gameID) throws DataAccessException;
}