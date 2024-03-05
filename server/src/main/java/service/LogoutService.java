package service;

import Result.LogoutResult;
import dataAccess.AuthDAO;
import model.LogoutRecord;

public class LogoutService {

    public Object logout(LogoutRecord token, AuthDAO auth){
        LogoutResult out = new LogoutResult(null);
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            auth.deleteAuth(key);
        } else {out = new LogoutResult("Error: unauthorized");
        }
        return out;
    }
}
