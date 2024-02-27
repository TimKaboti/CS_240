package dataAccess;
import model.UserData;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{

    public Map<String, UserData> userData = null;

    @Override
    public void clear() {
        userData.clear();
    }

    /** find out which methods should throw the dataAccessException **/

    @Override
    public void createUser(String username, String password, String email) {
        userData.put(username, new UserData(username,password,email));
    }

    @Override
    public UserData getUser(String username) {
        if (userData.get(username) == null) {
            return null;
        } else {
            return userData.get(username);
        }
    }



}
