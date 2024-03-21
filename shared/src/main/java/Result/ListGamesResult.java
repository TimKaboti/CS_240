package Result;

import com.google.gson.Gson;
import model.GameData;

import java.util.List;
import java.util.Map;

public record ListGamesResult(List<GameData> games, String message) {
    public ListGamesResult(List<GameData> games, String message) {
        this.games = games;
        this.message = message;
    }

    public String toString(){
        return this.message;
    }

}
