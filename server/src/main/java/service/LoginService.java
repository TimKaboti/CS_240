package service;

import Result.LoginResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.LoginRecord;

public class LoginService {

    public Object login (LoginRecord login, UserDAO userMemory, AuthDAO auth) throws DataAccessException {
        String username = login.username();
        String password = login.password();
        LoginResult log = new LoginResult(null, null, "Error: unauthorized");
        if (userMemory.getUser(username)) {
            if (userMemory.getUser(username) && userMemory.getPassword(username).equals(password)) {
                String token = auth.CreateAuth(username);
                log = new LoginResult(username, token, null);
            }
        }
        return log;
    }
}
