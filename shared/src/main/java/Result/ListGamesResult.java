package Result;

import model.GameData;

import java.util.List;

public record ListGamesResult(List<GameData> games, String message) {
}
