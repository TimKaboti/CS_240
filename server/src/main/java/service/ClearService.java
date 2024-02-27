package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.ClearRecord;

public class ClearService {

    public void clearServers(MemoryUserDAO userServer, MemoryAuthDAO authServer, MemoryGameDAO gameServer) {
        userServer.clear();
        authServer.clear();
        gameServer.clear();
    }

}
