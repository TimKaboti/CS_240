package Result;

import model.GameData;

import java.util.List;

public record ListGamesResult(List<GameData> games, String message) {
    public ListGamesResult(List<GameData> games, String message) {
        this.games = games;
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
