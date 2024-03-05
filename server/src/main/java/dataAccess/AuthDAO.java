package dataAccess;

public interface AuthDAO {

    public String CreateAuth(String username) throws DataAccessException;
    public boolean getAuth(String token) throws DataAccessException;
    public void deleteAuth(String token) throws DataAccessException;
    public void clear() throws DataAccessException;

}
