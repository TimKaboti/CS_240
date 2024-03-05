package dataAccess;
import chess.ChessGame;
import model.GameData;
import java.util.List;
import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class GameSQL implements GameDAO{
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createGame() throws DataAccessException {

    }

    @Override
    public ChessGame getGame(Integer ID) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame() throws DataAccessException {

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) throws DataAccessException {

    }

    @Override
    public boolean isNull(Integer gameID) throws DataAccessException {
        return false;
    }

    @Override
    public boolean taken(String color, Integer gameID) throws DataAccessException {
        return false;
    }
}
