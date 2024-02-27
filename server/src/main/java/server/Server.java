package server;

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
//        Spark.delete( "/db",((request, response) -> new ClearHandler().handle(request, response, UserDAO, GameDAO, AuthDAO)));
//        Spark.post( "/user",(((request, response) -> new RegisterHandler().handle(request, response, UserDAO))));
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
        var handle = new Gson().fromJson(req.body());
    }

    private void RegisterHandler(Request req, Response res) {
        var serializer = new Gson();
        var objFromJson = serializer.fromJson(json, Map.class);
    }

}
