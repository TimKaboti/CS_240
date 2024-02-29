package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
    public Map<Integer, GameData> gameData = new HashMap<>();


    @Override
    public void clear() {
        gameData.clear();
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
    public List<GameData> listGames() {
        List<GameData> list = new ArrayList<>();
        for (GameData value: gameData.values()) {
            list.add(value);
        }
        return list;
    }

    @Override
    public void updateGame() {

    }

    @Override
    public void joinGame(String username, String color, Integer gameID) {
        if (gameData.get(gameID) != null) {
            GameData game = gameData.get(gameID);
            game.renameUser(color, username);
        }
    }

    public boolean isNull (String name, Integer gameID){
        GameData game = gameData.get(gameID);
        if(Objects.equals(game.getBlackUsername(), name) || Objects.equals(game.getWhiteUsername(), name));{
            return true;
        }

    }

}

