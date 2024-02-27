package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.LoginRecord;

public class LoginService {

    public AuthData login (LoginRecord login, MemoryUserDAO userMemory, MemoryAuthDAO auth) {
        String username = login.username();
        String password = login.password();
        AuthData data = null;
        if (userMemory.userData.containsKey(username)) {
            String token = auth.CreateAuth(username);
            data = new AuthData(token, username);
        }
        return data;
    }
}
