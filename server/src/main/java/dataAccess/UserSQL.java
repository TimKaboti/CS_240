package dataAccess;

import model.UserData;

public class UserSQL implements UserDAO{
    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
