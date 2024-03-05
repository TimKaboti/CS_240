package dataAccess;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public abstract class MemoryUserDAO implements UserDAO{

    public Map<String, UserData> userData = new HashMap<>();

    @Override
    public void clear() {
        userData.clear();
    }

    /** find out which methods should throw the dataAccessException **/

    @Override
    public void createUser(UserData user) {
        userData.put(user.getUsername(), new UserData(user.getUsername(),user.getPassword(),user.getEmail()));
    }

    @Override
    public UserData getUser(String username) {
        if (userData.get(username) == null) {
            return null;
        } else {
            return userData.get(username);
        }
    }

    public String getPassword(String username) {
        UserData user = userData.get(username);
        if(user == null){
            return null;
        }
        return user.getPassword();
    }


}
