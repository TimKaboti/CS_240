package dataAccess;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
    public Map<Integer, GameData> gameData = new HashMap<>();


    @Override
    public void clear() {
        gameData.clear();
    }
    @Override
    public void createGame(Integer ID, GameData data) {
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
    public List<GameData> listGames() {
        List<GameData> list = new ArrayList<>();
        for (GameData value: gameData.values()) {
            list.add(value);
        }
        return list;
    }

    @Override
    public void updateGame(ChessGame game, ChessMove move, Integer gameID) {

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) {
        if (gameData.get(gameID) != null) {
            GameData game = gameData.get(gameID);
            game.renameUser(color, username);
        }
    }

    public boolean isNull (Integer gameID){
        GameData game = gameData.get(gameID);
        return game != null;
    }

    @Override
    public boolean isNotNull(Integer gameID) throws DataAccessException {
        return false;
    }

    public boolean taken(String color, Integer gameID) {
        boolean bool = false;
        GameData game = gameData.get(gameID);
        if (Objects.equals(color, "BLACK") && game.getBlackUsername() != null) {
            bool = true;
        }
        {
            }
        if (Objects.equals(color, "WHITE") && game.getWhiteUsername() != null) {
            bool = true;
        }
        {
            }
        return bool;
    }

}

