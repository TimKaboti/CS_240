package service;

import Result.RegisterResult;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import model.RegisterRecord;

public class RegistrationService {
    public Object register(RegisterRecord user, UserDAO memory, AuthDAO auth) {
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
