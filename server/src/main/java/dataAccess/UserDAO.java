package dataAccess;

import model.UserData;

public interface UserDAO {

    public UserData getUser(String username) throws DataAccessException;

    public String getPassword(String username) throws DataAccessException;

    public void createUser(String username, String password, String email) throws DataAccessException;

    public void clear() throws DataAccessException;
}
