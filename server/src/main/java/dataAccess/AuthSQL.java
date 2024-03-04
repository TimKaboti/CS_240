package dataAccess;

import model.AuthData;

public class AuthSQL implements AuthDAO{
    @Override
    public String CreateAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String token) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
