package dataAccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import model.GameData;

import java.util.*;

public interface GameDAO {

    public void clear() throws DataAccessException;
    void createGame(Integer ID, GameData data) throws DataAccessException;

    public ChessGame getGame(Integer ID) throws DataAccessException;
    public List<GameData> listGames() throws DataAccessException;
    public void updateGame(ChessGame game, ChessMove move, Integer gameID) throws DataAccessException, InvalidMoveException;
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException;
    public boolean isNull (Integer gameID) throws DataAccessException;

    /**
     * returns true if a game with the provided gameID exists.
     * false otherwise.
     */
    boolean isNotNull(Integer gameID) throws DataAccessException;

    public boolean taken (String color, Integer gameID) throws DataAccessException;

    public String whitePlayerName (Integer gameID) throws DataAccessException;

    public String blackPlayerName (Integer gameID) throws DataAccessException;

    public void setBlackPlayerNull (Integer gameID) throws DataAccessException;

    public void setWhitePlayerNull (Integer gameID) throws DataAccessException;

    public void updateGameState(ChessGame thisGame, Integer gameID) throws DataAccessException;
}