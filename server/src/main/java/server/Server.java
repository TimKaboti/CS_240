package server;
import Result.CreateGameResult;
import Result.HandlerResult;
import Result.JoinGameResult;
import model.*;
import service.*;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Objects;

public class Server {

    public int run(int desiredPort) {
        MemoryUserDAO UserDAO = new MemoryUserDAO();
        MemoryGameDAO GameDAO = new MemoryGameDAO();
        MemoryAuthDAO AuthDAO = new MemoryAuthDAO();
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

    private Object ClearHandler(Request req, Response res, MemoryUserDAO user, MemoryGameDAO game, MemoryAuthDAO auth) {
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

    private Object RegisterHandler(Request req, Response res, MemoryUserDAO memory, MemoryAuthDAO auth) {
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

    private Object LoginHandler(Request req, Response res, MemoryUserDAO userMemory, MemoryAuthDAO auth) {
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

    private Object LogoutHandler(Request req, Response res, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        LogoutRecord logoutRecord = serializer.fromJson(req.headers("Authorization"), LogoutRecord.class);
        LogoutService logoutService = new LogoutService();
        var result = logoutService.logout(logoutRecord, auth);
        String message = result.toString();
        if("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: description".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object ListGameHandler(Request req, Response res, MemoryGameDAO gameMemory, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        ListGameRecord listGameRecord = serializer.fromJson(req.headers("Authorization"), ListGameRecord.class);
        ListGamesService listGamesService = new ListGamesService();
        var result = listGamesService.gameList(listGameRecord, gameMemory, auth);
        String message = result.toString();
        if("Error: unauthorized".equals(message)) { res.status(401);}
        else if ("Error: description".equals(message)) { res.status(500);}
        else{ res.status(200);}
        return new Gson().toJson(result);
    }

    private Object CreateGameHandler(Request req, Response res, MemoryGameDAO gameMemory, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        CreateGameRecord createGameRecord = serializer.fromJson(req.body(), CreateGameRecord.class);
        CreateGameService createGameService = new CreateGameService();
        String authToken = req.headers("Authorization");
        Object result = (null);
        if (auth.authData.containsKey(authToken)) {
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

    private Object JoinGameHandler(Request req, Response res, MemoryGameDAO gameMemory, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        JoinGameRecord joinGameRecord = serializer.fromJson(req.body(), JoinGameRecord.class);
        JoinGameService joinGameService = new JoinGameService();
        String authToken = req.headers("Authorization");
        Object result = (null);
        if (auth.authData.containsKey(authToken)) {
            result = joinGameService.joinGame(joinGameRecord, gameMemory, String.valueOf(auth.authData.get(authToken)), auth);
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
