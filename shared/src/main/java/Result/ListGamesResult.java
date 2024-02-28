package Result;

import model.GameData;

import java.util.List;

public record ListGamesResult(String games, List<GameData> list) {
}
