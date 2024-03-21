package clientTests;

import Result.*;
import dataAccess.*;
import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import service.*;
import ui.ClientCommunicator;
import ui.ResponseException;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void testFacadeClearSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ClearRecord record = new ClearRecord();
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        ClearService clearService = new ClearService();
        ClearResult result = clearService.clearServers(UserDAO,AuthDAO,GameDAO);

        // Verify the result
        assertNotNull(result);

        // Negative test case: trying to clear with invalid authentication token
        assertThrows(ResponseException.class, () -> clearService.clearServers(UserDAO,AuthDAO,GameDAO));
    }

    @Test
    public void testFacadeCreateSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        CreateGameRecord record = new CreateGameRecord("gameName");
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        CreateGameService createGameService = new CreateGameService();
        CreateGameResult result = (CreateGameResult) createGameService.newGame(record,GameDAO);

        // Verify the result
        assertNotNull(result);

        // Negative test case: trying to create a game with invalid authentication token
        assertThrows(ResponseException.class, () -> createGameService.newGame(record,GameDAO));
    }

    @Test
    public void testFacadeJoinSuccess() throws ResponseException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        JoinGameRecord record = new JoinGameRecord("white",1234);
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        JoinGameService joinGameService = new JoinGameService();
        JoinGameResult result = joinGameService.joinGame(record,GameDAO, "AuthToken",AuthDAO);

        // Verify the result
        assertNotNull(result);

        // Negative test case: trying to join a game with invalid authentication token
        assertThrows(ResponseException.class, () -> joinGameService.joinGame(record,GameDAO,"AuthToken",AuthDAO));
    }

    @Test
    public void testFacadeListSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ListGameRecord record = new ListGameRecord("AuthToken");
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        ListGamesService listGamesService = new ListGamesService();
        ListGamesResult result = (ListGamesResult) listGamesService.gameList(record,GameDAO,AuthDAO);

        // Verify the result
        assertNotNull(result);


        // Negative test case: trying to list games with invalid authentication token
        assertThrows(ResponseException.class, () -> listGamesService.gameList(record,GameDAO,AuthDAO));
    }

    @Test
    public void testFacadeLoginSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LoginRecord record = new LoginRecord("username", "password");
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        LoginService loginService = new LoginService();
        Object result = loginService.login(record,UserDAO, AuthDAO);

        // Verify the result
        assertNotNull(result);


        // Negative test case: trying to login with invalid credentials
        assertThrows(ResponseException.class, () -> loginService.login(record,UserDAO,AuthDAO));
    }

    @Test
    public void testFacadeLogoutSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LogoutRecord record = new LogoutRecord("authToken");
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        LogoutService logoutService = new LogoutService();
        Object result = logoutService.logout(record, AuthDAO);

        // Verify the result
        assertNotNull(result);

        // Negative test case: trying to logout with invalid authentication token
        assertThrows(ResponseException.class, () -> logoutService.logout(record, AuthDAO));
    }

    @Test
    public void testFacadeRegisterSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        UserDAO UserDAO = new UserSQL();
        GameDAO GameDAO = new GameSQL();
        AuthDAO AuthDAO = new AuthSQL();
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        RegisterRecord record = new RegisterRecord("username", "password", "email");
        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        RegistrationService registerService = new RegistrationService();
        RegisterResult result = new RegisterResult("username", "password", "email");

        // Verify the result
        assertNotNull(result);

        // Negative test case: trying to register with an existing username
        assertThrows(ResponseException.class, () -> registerService.register(record,UserDAO,AuthDAO));
    }
}