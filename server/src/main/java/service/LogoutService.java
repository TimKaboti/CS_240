package service;

import Result.LogoutResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.LogoutRecord;

public class LogoutService {

    public Object logout(LogoutRecord token, AuthDAO auth) throws DataAccessException {
        LogoutResult out = new LogoutResult(null);
        String key = token.authToken();
        if (auth.getAuth(key)){
            auth.deleteAuth(key);
        } else {out = new LogoutResult("Error: unauthorized");
        }
        return out;
    }
}
