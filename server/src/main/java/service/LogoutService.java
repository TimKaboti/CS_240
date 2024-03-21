package service;

import Result.ClearResult;
import Result.LogoutResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.LogoutRecord;

public class LogoutService {

    public Object logout(LogoutRecord token, AuthDAO auth) throws DataAccessException {
        LogoutResult out = new LogoutResult(null);
        String key = token.authToken();
        if (auth.getAuth(key)){
            try{auth.deleteAuth(key);}
            catch (DataAccessException e) { out = new LogoutResult("Error: logout service failed");}
        } else {out = new LogoutResult("Error: unauthorized");
        }
        return out;
    }
}
