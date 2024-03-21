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
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ClearResult result = facade.facadeClear(new ClearRecord());
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: clear service failed");
    }

    @Test
    public void testFacadeClearInvalidToken() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeClear(new ClearRecord()));
    }

    @Test
    public void testFacadeCreateSuccess() throws ResponseException, DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        CreateGameResult result = facade.facadeCreate(new CreateGameRecord("gameName"));
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: create handler failed");
        assertNotEquals(result.message(), "Error: bad request");
    }

    @Test
    public void testFacadeCreateInvalidToken() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeCreate(new CreateGameRecord("gameName")));
    }

    @Test
    public void testFacadeJoinSuccess() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        JoinGameResult result = facade.facadeJoin(new JoinGameRecord("white", 1234));
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: join handler failed");
        assertNotEquals(result.message(), "Error: bad request");
        assertNotEquals(result.message(), "Error: already taken");
    }

    @Test
    public void testFacadeJoinInvalidToken() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeJoin(new JoinGameRecord("white", 1234)));
    }

    @Test
    public void testFacadeListSuccess() throws ResponseException, DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        ListGamesResult result = facade.facadeList();
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
        assertNotEquals(result.message(), "Error: list handler failed");
    }

    @Test
    public void testFacadeListInvalidToken() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeList());
    }

    @Test
    public void testFacadeLoginSuccess() throws ResponseException, DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LoginRecord record = new LoginRecord("username", "password");
        LoginResult result = facade.facadeLogin(new LoginRecord("username", "password"));
        assertNotNull(result);
        assertEquals(record.username(), result.username());
        assertNotEquals(result.message(), "Error: unauthorized");
    }

    @Test
    public void testFacadeLoginInvalidCredentials() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeLogin(new LoginRecord("invalidUsername", "invalidPassword")));
    }

    @Test
    public void testFacadeLogoutSuccess() throws ResponseException, DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        LogoutResult result = facade.facadeLogout(new LogoutRecord("authToken"));
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
    }

    @Test
    public void testFacadeLogoutInvalidToken() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeLogout(new LogoutRecord("invalidAuthToken")));
    }

    @Test
    public void testFacadeRegisterSuccess() throws ResponseException, DataAccessException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        RegisterResult result = facade.facadeRegister(new RegisterRecord("username", "password", "email"));
        assertNotNull(result);
        assertNotEquals(result.message(), "Error: unauthorized");
    }

    @Test
    public void testFacadeRegisterInvalidUsername() throws ResponseException {
        ServerFacade facade = new ServerFacade("http://localhost:8080");
        assertThrows(ResponseException.class, () -> facade.facadeRegister(new RegisterRecord("", "password", "email")));
    }

}