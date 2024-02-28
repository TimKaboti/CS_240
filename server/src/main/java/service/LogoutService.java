package service;

import Result.LogoutResult;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.LogoutRecord;

public class LogoutService {

    public Object logout(LogoutRecord token, MemoryAuthDAO auth){
        LogoutResult out = new LogoutResult();
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            auth.deleteAuth(key);
        } /** may need an else statement here. set the data to not null. doesn't matter. **/
        return out;
    }
}
