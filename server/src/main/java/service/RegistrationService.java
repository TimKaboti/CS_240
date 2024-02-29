package service;

import Result.RegisterResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.RegisterRecord;
import model.UserData;

public class RegistrationService {
    public Object register(RegisterRecord user, MemoryUserDAO memory, MemoryAuthDAO auth) {
        RegisterResult result;
        if(memory.getUser(user.username()) == null) {
            if(user.username() == null || user.password() == null || user.email() == null) {
                result = new RegisterResult(null, null, "Error: bad request");
            } else {
                memory.createUser(user.username(), user.password(), user.email());
                String token = auth.CreateAuth(user.username());
                result = new RegisterResult(user.username(), token, null);
            }
        } else {
            result = new RegisterResult(null, null, "Error: already taken");
        }
        return result;
    }
}
