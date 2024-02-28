package service;

import Result.LogoutResult;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.LogoutRecord;

public class LogoutService {

    public Object logout(LogoutRecord token, MemoryAuthDAO auth){
        LogoutResult out = new LogoutResult(null);
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            auth.deleteAuth(key);
        } else {out = new LogoutResult("Error: unauthorized");
        }
        return out;
    }
}
