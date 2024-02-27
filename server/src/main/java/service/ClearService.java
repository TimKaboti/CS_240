package service;

import dataAccess.MemoryUserDAO;
import model.ClearRecord;

public class ClearService {
    MemoryUserDAO server;
    public ClearService(MemoryUserDAO server) {
        this.server = server;
    }
    public void clearMemory() {
        server.clear();
    }
}
