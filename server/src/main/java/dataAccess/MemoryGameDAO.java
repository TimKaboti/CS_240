package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    public Map<Integer, GameData> gameData = null;


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
    public void joinGame(String username, String color, Integer gameID) {
        if (gameData.get(gameID) != null) {
            GameData game = gameData.get(gameID);
            game.renameUser(color, username);
        }
    }

}
