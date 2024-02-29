package service;

import Result.ClearResult;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.ClearRecord;

public class ClearService {

    public Object clearServers(MemoryUserDAO userServer, MemoryAuthDAO authServer, MemoryGameDAO gameServer) {
        ClearResult cleared = new ClearResult(null);
        userServer.clear();
        authServer.clear();
        gameServer.clear();
        return cleared;
    }

}
