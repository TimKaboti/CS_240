package service;

import Result.LoginResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.LoginRecord;

public class LoginService {

    public Object login (LoginRecord login, MemoryUserDAO userMemory, MemoryAuthDAO auth) {
        String username = login.username();
        String password = login.password();
        LoginResult log = new LoginResult(null, null);
        if (userMemory.userData.containsKey(username)) {
            String token = auth.CreateAuth(username);
            log = new LoginResult(username, token);
        }
        return log;
    }
}
