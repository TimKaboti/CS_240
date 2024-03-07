package service;

import Result.ClearResult;
import dataAccess.*;

public class ClearService {

    public Object clearServers(UserDAO userServer, AuthDAO authServer, GameDAO gameServer) throws DataAccessException {
        ClearResult cleared = new ClearResult(null);
        try{userServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: description");
        }
        try{authServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: description");
        }
        try{gameServer.clear();}
        catch (DataAccessException e) { cleared = new ClearResult("Error: description");
        }
        return cleared;
    }

}
