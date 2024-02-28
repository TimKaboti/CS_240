package dataAccess;

import chess.ChessGame;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public Map<String, AuthData> authData = new HashMap<>();


    @Override
    public void clear() {
        authData.clear();
    }
    @Override
    /** creates an authToken and adds it paired with the username to authData. returns the token. **/
    public String CreateAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        authData.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String token) {
        return authData.get(token);
    }

    @Override
    public void deleteAuth(String token) {
        authData.remove(token);

    }

}