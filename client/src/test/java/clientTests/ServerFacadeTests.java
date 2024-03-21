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
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ClearRecord record = new ClearRecord();
//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        ClearResult result = facade.facadeClear(record);

        // Verify the result
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: clear service failed");

        // Negative test case: trying to clear with invalid authentication token
        ClearService clearService = null;
        assertThrows(ResponseException.class, () -> facade.facadeClear(record));
    }

    @Test
    public void testFacadeCreateSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        CreateGameRecord record = new CreateGameRecord("gameName");
//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        CreateGameResult result = facade.facadeCreate(record);

        // Verify the result
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: create handler failed");
        assertNotEquals(result.message(), "Error: bad request");
        assertNotNull(facade.facadeList(), "authToken invalid after login");


        // Negative test case: trying to create a game with invalid authentication token
        assertThrows(ResponseException.class, () -> facade.facadeCreate(record));
    }

    @Test
    public void testFacadeJoinSuccess() throws ResponseException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        JoinGameRecord record = new JoinGameRecord("white",1234);
//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        JoinGameResult result = facade.facadeJoin(record);

        // Verify the result
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: join handler failed");
        assertNotEquals(result.message(), "Error: bad request");
        assertNotEquals(result.message(), "Error: already taken");
        assertNotNull(facade.facadeList(), "authToken invalid after login");


        // Negative test case: trying to join a game with invalid authentication token
        assertThrows(ResponseException.class, () -> facade.facadeJoin(record));
    }

    @Test
    public void testFacadeListSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        String token = "AuthToken";
        JoinGameRecord joinRecord = new JoinGameRecord("white",1234);

//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        ListGamesResult result = facade.facadeList();

        // Verify the result
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: list handler failed");
        assertNotNull(facade.facadeJoin(joinRecord), "authToken invalid after login");



        // Negative test case: trying to list games with invalid authentication token
        assertThrows(ResponseException.class, () -> facade.facadeList());
    }

    @Test
    public void testFacadeLoginSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LoginRecord record = new LoginRecord("username", "password");
        LoginRecord newRecord = new LoginRecord("usernme", "pasword");

//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        LoginResult result = facade.facadeLogin(record);

        // Verify the result
        assertNotNull(result);
        assertEquals(record.username(), result.username());
        assertNotNull(facade.facadeList(), "authToken invalid after login");

        // Negative test case: trying to login with invalid credentials
        assertThrows(ResponseException.class, () -> facade.facadeLogin(newRecord));
    }

    @Test
    public void testFacadeLogoutSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LogoutRecord record = new LogoutRecord("authToken");
//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        LogoutResult result = facade.facadeLogout(record);

        // Verify the result
        assertNotNull(result);
        assertNotNull(facade.facadeList(), "authToken valid after logout");


        // Negative test case: trying to logout with invalid authentication token
        assertThrows(ResponseException.class, () -> facade.facadeLogout(record));
    }

    @Test
    public void testFacadeRegisterSuccess() throws ResponseException, DataAccessException {
        // Mock objects
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        RegisterRecord record = new RegisterRecord("username", "password", "email");
//        ClientCommunicator communicator = new  ClientCommunicator("http://localhost:8080");

        // Call the method
        RegisterResult result = facade.facadeRegister(record);

        // Verify the result
        assertNotNull(result);
        assertEquals(record.username(), result.username());
        assertNotNull(facade.facadeList(), "authToken invalid after registration");

        // Negative test case: trying to register with an existing username
        assertThrows(ResponseException.class, () -> facade.facadeRegister(record));
    }

}