package service;

import dataAccess.MemoryDAO;
import model.ClearRecord;

public class ClearService {
    MemoryDAO server;
    public ClearService(MemoryDAO server) {
        this.server = server;
    }
    public void clearMemory() {
        server.clear();
    }
}
