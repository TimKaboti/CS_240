package server;
import Request.RegisterRequest;
import Request.LoginRequest;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.RegisterRecord;
import service.RegistrationService;
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
//        Spark.delete( "/db",((request, response) -> new ClearHandler().handle(request, response, UserDAO, GameDAO, AuthDAO)));
        Spark.post( "/user",(((request, response) -> RegisterHandler(request, response, UserDAO, AuthDAO))));
//        Spark.post( "/session",(((request, response) -> new LoginHandler().handle(request, response, UserDAO, AuthDAO))));
//        Spark.delete( "/session",(((request, response) -> new LogoutHandler().handle(request, response, UserDAO, AuthDAO))));
//        Spark.get( "/game",(((request, response) -> new ListGameHandler().handle(request, response, GameDAO, AuthDAO))));
//        Spark.post( "/game",(((request, response) -> new CreateGameHandler().handle(request, response, GameDAO))));
//        Spark.put( "/game",(((request, response) -> new JoinGameHandler().handle(request, response, GameDAO, AuthDAO))));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void ClearHandler(Request req, Response res, MemoryUserDAO user, MemoryGameDAO game, MemoryAuthDAO auth) {
//        handle = new Gson().fromJson(req.body(), );
    }

    private Object RegisterHandler(Request req, Response res, MemoryUserDAO memory, MemoryAuthDAO auth) {
       Gson serializer = new Gson();
       RegisterRecord registerRecord = serializer.fromJson(req.body(), RegisterRecord.class);
       RegistrationService registrationService = new RegistrationService();
       var result = registrationService.register(registerRecord, memory, auth);
       return new Gson().toJson(result);
    }

}
