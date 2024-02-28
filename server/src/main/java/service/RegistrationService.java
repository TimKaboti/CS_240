package service;

import Result.RegisterResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.RegisterRecord;
import model.UserData;

public class RegistrationService {
    public Object register(RegisterRecord user, MemoryUserDAO memory, MemoryAuthDAO auth) {
        memory.createUser(user.username(), user.password(), user.email());
        String token = auth.CreateAuth(user.username());
        RegisterResult result = new RegisterResult(user.username(), token);
        return result;
    }
}
