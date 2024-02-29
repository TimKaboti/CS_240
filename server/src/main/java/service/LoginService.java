package service;

import Result.LoginResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.LoginRecord;

import java.util.Objects;

public class LoginService {

    public Object login (LoginRecord login, MemoryUserDAO userMemory, MemoryAuthDAO auth) {
        String username = login.username();
        String password = login.password();
        LoginResult log;
        if (userMemory.userData.containsKey(username) || Objects.equals(userMemory.getPassword(username), password)) {
            String token = auth.CreateAuth(username);
            log = new LoginResult(username, token, null);
        } else {log = new LoginResult(null, null, "Error: unauthorized");}
        return log;
    }
}
