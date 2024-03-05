package service;

import Result.ClearResult;
import dataAccess.*;

public class ClearService {

    public Object clearServers(UserDAO userServer, AuthDAO authServer, GameDAO gameServer) {
        ClearResult cleared = new ClearResult(null);
        userServer.clear();
        authServer.clear();
        gameServer.clear();
        return cleared;
    }

}
