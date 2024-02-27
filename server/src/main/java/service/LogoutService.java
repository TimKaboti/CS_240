package service;

import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.LogoutRecord;

public class LogoutService {

    public AuthData logout(LogoutRecord token, MemoryAuthDAO auth){
        AuthData data = null;
        String key = token.authToken();
        if (auth.authData.containsKey(key)){
            auth.deleteAuth(key);
        } /** may need an else statement here. set the data to not null. doesn't matter. **/
        return data;
    }
}
