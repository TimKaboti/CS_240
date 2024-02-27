package dataAccess;

import model.AuthData;

public interface AuthDAO {

    public String CreateAuth(String username) throws DataAccessException;
    public AuthData getAuth(String token) throws DataAccessException;
    public void deleteAuth(String token) throws DataAccessException;
    public void clear() throws DataAccessException;

}
