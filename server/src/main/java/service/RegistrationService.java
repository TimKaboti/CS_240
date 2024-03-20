package service;

import Result.ClearResult;
import Result.RegisterResult;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.RegisterRecord;
import model.UserData;

public class RegistrationService {
    public Object register(RegisterRecord user, UserDAO userData, AuthDAO authData) throws DataAccessException {
        UserData tempUser = new UserData(user.username(), user.password(), user.email());
        RegisterResult result;
        if(!userData.getUser(user.username())) {
            if(user.username() == null || user.password() == null || user.email() == null) {
                result = new RegisterResult(null, null, "Error: bad request");
            } else {
               try{ userData.createUser(tempUser);
                String token = authData.CreateAuth(user.username());
                result = new RegisterResult(user.username(), token, null);}
               catch (DataAccessException e) { result = new RegisterResult(null, null, "Error: register service failed");}
            }
        } else {
            result = new RegisterResult(null, null, "Error: already taken");
        }
        return result;
    }
}
