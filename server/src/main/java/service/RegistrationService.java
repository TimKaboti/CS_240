package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.RegisterRecord;
import model.UserData;

public class RegistrationService {
    public AuthData register(RegisterRecord user, MemoryUserDAO memory, MemoryAuthDAO auth) {
        AuthData data;
        memory.createUser(user.username(), user.password(), user.email());
        String token = auth.CreateAuth(user.username());
        data = new AuthData(token, user.username());
        return data;
    }
}
