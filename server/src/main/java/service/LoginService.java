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
        LoginResult log = new LoginResult(null, null, "Error: unauthorized");
        if (userMemory.userData.containsKey(username)) {
            if (userMemory.userData.containsKey(username) && userMemory.getPassword(username).equals(password)) {
                String token = auth.CreateAuth(username);
                log = new LoginResult(username, token, null);
            }
        }
        return log;
    }
}
