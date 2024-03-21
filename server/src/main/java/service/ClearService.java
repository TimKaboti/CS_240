package service;

import Result.ClearResult;
import dataAccess.*;

public class ClearService {

    public ClearResult clearServers(UserDAO userServer, AuthDAO authServer, GameDAO gameServer) throws DataAccessException {
        ClearResult cleared = new ClearResult(null);
        try{userServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: clear service failed");
        }
        try{authServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: clear service failed");
        }
        try{gameServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: clear service failed");
        }
        return cleared;
    }

}
