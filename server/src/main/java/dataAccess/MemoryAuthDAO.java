package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public Map<String, AuthData> authData = new HashMap<>();


    @Override
    public void clear() {
        authData.clear();
    }
    @Override
    /** creates an authToken and adds it paired with the username to authData. returns the token. **/
    public String CreateAuth(String username) {
        String authToken = null;
        int num = 0;

        boolean uniqueTokenFound = false;

        while (!uniqueTokenFound) {
            authToken = UUID.randomUUID().toString();
            boolean authTokenExists = false;

            // Check if the generated authToken already exists
            for (AuthData value : authData.values()) {
                if (value.getAuthToken().equals(authToken)) {
                    authTokenExists = true;
                    break;  // Break out of the loop if a match is found
                }
            }

            // If the authToken is unique, set the flag to true
            if (!authTokenExists) {
                uniqueTokenFound = true;
            }
        }

        authData.put(authToken, new AuthData(authToken, username));
        return authToken;
    }


    @Override
    public boolean getAuth(String token) {
        //return authData.get(token).toString();
        return true;
    }

    @Override
    public void deleteAuth(String token) {
        authData.remove(token);

    }

}