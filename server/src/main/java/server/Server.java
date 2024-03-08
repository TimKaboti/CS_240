package server;
import Result.CreateGameResult;
import Result.JoinGameResult;
import dataAccess.*;
import model.*;
import service.*;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;
import static dataAccess.DatabaseManager.createDatabase;

public class Server {

    public int run(int desiredPort) {
        try {
            //createDatabase();
            DatabaseManager.configureDatabase();
        } catch(DataAccessException e) {
//            e.printStackTrace();
//            return -1;
        }


        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.delete( "/db",((request, response) -> ClearHandler(request, response, UserDAO, GameDAO, AuthDAO)));
        Spark.post( "/user",(((request, response) -> RegisterHandler(request, response, UserDAO, AuthDAO))));
        Spark.post( "/session",(((request, response) -> LoginHandler(request, response, UserDAO, AuthDAO))));
        Spark.delete( "/session",(((request, response) -> LogoutHandler(request, response, AuthDAO))));
        Spark.get( "/game",(((request, response) -> ListGameHandler(request, response, GameDAO, AuthDAO))));
        Spark.post( "/game",(((request, response) -> CreateGameHandler(request, response, GameDAO, AuthDAO))));
        Spark.put( "/game",(((request, response) -> JoinGameHandler(request, response, GameDAO, AuthDAO))));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object ClearHandler(Request req, Response res, UserDAO user, GameDAO game, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        ClearRecord clearRecord = serializer.fromJson(req.body(), ClearRecord.class);
        ClearService clearService = new ClearService();
        clearService.clearServers(user, auth, game);
        String message = clearService.toString();
        if("Error: description".equals(message)) { res.status(500);}
        else { res.status(200);}
        return new Gson().toJson(clearService);
    }

//    res.setstatus(400)
//    unauthorized == bad authToken
//    bad request == bad input ie user already exists
//

    private Object RegisterHandler(Request req, Response res, UserDAO memory, AuthDAO auth) throws DataAccessException {
       Gson serializer = new Gson();
       RegisterRecord registerRecord = serializer.fromJson(req.body(), RegisterRecord.class);
       RegistrationService registrationService = new RegistrationService();
       var result = registrationService.register(registerRecord, memory, auth);
       String message = result.toString();
       if("Error: bad request".equals(message)) { res.status(400);}
       else if ("Error: already taken".equals(message)) { res.status(403);}
       else{ res.status(200);}
       return new Gson().toJson(result);
    }

    private Object LoginHandler(Request req, Response res, UserDAO userMemory, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        LoginRecord loginRecord = serializer.fromJson(req.body(), LoginRecord.class);
        LoginService loginService = new LoginService();
        var result = loginService.login(loginRecord, userMemory, auth);
        String message = result.toString();
        if("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: description".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object LogoutHandler(Request req, Response res, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        LogoutRecord logoutRecord = new LogoutRecord(req.headers("Authorization"));
        LogoutService logoutService = new LogoutService();
        var result = logoutService.logout(logoutRecord, auth);
        String message = result.toString();
        if("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: description".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object ListGameHandler(Request req, Response res, GameDAO gameMemory, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        ListGameRecord listGameRecord = new ListGameRecord (req.headers("Authorization"));
        ListGamesService listGamesService = new ListGamesService();
        var result = listGamesService.gameList(listGameRecord, gameMemory, auth);
        String message = result.toString();
        if("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: description".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object CreateGameHandler(Request req, Response res, GameDAO gameMemory, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        CreateGameRecord createGameRecord = serializer.fromJson(req.body(), CreateGameRecord.class);
        CreateGameService createGameService = new CreateGameService();
        String authToken = req.headers("Authorization");
        Object result = (null);
        if (auth.getAuth(authToken)) {
            result = createGameService.newGame(createGameRecord, gameMemory);
        }
        else { result = new CreateGameResult(null, "Error: unauthorized");}
        String message = result.toString();
        if("Error: bad request".equals(message)) { res.status(400);} // user does not provide gamename
        else if ("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: desription".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object JoinGameHandler(Request req, Response res, GameDAO gameMemory, AuthDAO auth) throws DataAccessException {
        Gson serializer = new Gson();
        JoinGameRecord joinGameRecord = serializer.fromJson(req.body(), JoinGameRecord.class);
        JoinGameService joinGameService = new JoinGameService();
        String authToken = req.headers("Authorization");
        Object result = (null);
        if (auth.getAuth(authToken)) {
            result = joinGameService.joinGame(joinGameRecord, gameMemory,  authToken, auth);
        }
        else { result = new JoinGameResult("Error: unauthorized");}
        String message = result.toString();
        if("Error: bad request".equals(message)) { res.status(400);} // check game id
        else if ("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: already taken".equals(message)) { res.status(403);} //color already taken
        else if ("Error: desription".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

}
