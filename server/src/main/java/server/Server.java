package server;
import Request.RegisterRequest;
import Request.LoginRequest;
import model.*;
import service.*;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Map;

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
//        Spark.get( "/game",(((request, response) -> ListGameHandler(request, response, GameDAO, AuthDAO))));
//        Spark.post( "/game",(((request, response) -> CreateGameHandler(request, response, GameDAO))));
//        Spark.put( "/game",(((request, response) -> JoinGameHandler(request, response, GameDAO, AuthDAO))));
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
        return new Gson().toJson(clearRecord);
    }

    private Object RegisterHandler(Request req, Response res, MemoryUserDAO memory, MemoryAuthDAO auth) {
       Gson serializer = new Gson();
       RegisterRecord registerRecord = serializer.fromJson(req.body(), RegisterRecord.class);
       RegistrationService registrationService = new RegistrationService();
       var result = registrationService.register(registerRecord, memory, auth);
       return new Gson().toJson(result);
    }

    private Object LoginHandler(Request req, Response res, MemoryUserDAO userMemory, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        LoginRecord loginRecord = serializer.fromJson(req.body(), LoginRecord.class);
        LoginService loginService = new LoginService();
        var result = loginService.login(loginRecord, userMemory, auth);
        return new Gson().toJson(result);
    }

    private Object LogoutHandler(Request req, Response res, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        LogoutRecord logoutRecord = serializer.fromJson(req.body(), LogoutRecord.class);
        LogoutService logoutService = new LogoutService();
        var result = logoutService.logout(logoutRecord, auth);
        return new Gson().toJson(result);
    }

    private Object ListGameHandler(Request req, Response res, MemoryGameDAO gameMemory, MemoryAuthDAO auth) {
        Gson serializer = new Gson();
        ListGameRecord listGameRecord = serializer.fromJson(req.headers("Authorization"), ListGameRecord.class);
        ListGamesService listGamesService = new ListGamesService();
        var result = listGamesService.gameList(listGameRecord, gameMemory, auth);
        return new Gson().toJson(result);
    }

}
