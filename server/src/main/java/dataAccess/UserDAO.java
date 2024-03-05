package dataAccess;

import model.UserData;

public interface UserDAO {

    public boolean getUser(String username) throws DataAccessException;

    public String getPassword(String username) throws DataAccessException;

    public String getEmail(String username) throws DataAccessException;

    public void createUser(UserData user) throws DataAccessException;

    public void clear() throws DataAccessException;
}
