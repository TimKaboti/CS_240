package clientTests;

import Result.*;
import dataAccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.ResponseException;
import ui.ServerFacade;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

  private static Server server;
  private static int port;

  @BeforeAll
  public static void init() {
    server=new Server();
    port=server.run(0);
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
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    ClearResult result=facade.facadeClear(new ClearRecord());
    assertNotNull(result);
    assertNotEquals(result.message(), "Error: clear service failed");
  }

  @Test
  public void testFacadeClearInvalidToken() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    RegisterResult result=facade.facadeRegister(new RegisterRecord("user", "password", "email"));
    facade.facadeClear(new ClearRecord());
    assertThrows(ResponseException.class, facade::facadeList);
  }

  @Test
  public void testFacadeCreateSuccess() throws ResponseException, DataAccessException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    CreateGameResult result=facade.facadeCreate(new CreateGameRecord("gameName"));
    assertNotNull(result);
    assertNotEquals(result.message(), "Error: unauthorized");
    assertNotEquals(result.message(), "Error: create handler failed");
    assertNotEquals(result.message(), "Error: bad request");
  }

  @Test
  public void testFacadeCreateInvalidToken() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    assertThrows(ResponseException.class, () -> facade.facadeCreate(new CreateGameRecord("gameName")));
  }

  @Test
  public void testFacadeJoinSuccess() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    facade.facadeCreate(new CreateGameRecord("gameName"));
    ListGamesResult listResult=facade.facadeList();
    int randomIndex=getRandomIndex(listResult.games().size());
    int id=listResult.games().get(randomIndex).getGameID();
    JoinGameResult result=facade.facadeJoin(new JoinGameRecord("white", id));
    assertNotNull(result);
    assertNotEquals(result.message(), "Error: unauthorized");
    assertNotEquals(result.message(), "Error: join handler failed");
    assertNotEquals(result.message(), "Error: bad request");
    assertNotEquals(result.message(), "Error: already taken");

  }

  @Test
  public void testFacadeJoinInvalidToken() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    RegisterResult result=facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    facade.facadeCreate(new CreateGameRecord("gameName"));
    ListGamesResult listResult=facade.facadeList();
    int randomIndex=getRandomIndex(listResult.games().size());
    int id=listResult.games().get(randomIndex).getGameID();
    facade.facadeJoin(new JoinGameRecord("white", id));
    facade.facadeLogout(new LogoutRecord(result.authToken()));
    facade.facadeRegister(new RegisterRecord("name", "pass", "email"));
    assertThrows(ResponseException.class, () -> facade.facadeJoin(new JoinGameRecord("white", id)));
  }

  @Test
  public void testFacadeListSuccess() throws ResponseException, DataAccessException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    facade.facadeCreate(new CreateGameRecord("gameName"));
    ListGamesResult result=facade.facadeList();
    assertNotNull(result);
    assertNotEquals(result.message(), "Error: unauthorized");
    assertNotEquals(result.message(), "Error: list handler failed");
  }

  @Test
  public void testFacadeListInvalidToken() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    assertThrows(ResponseException.class, () -> facade.facadeList());
  }

  @Test
  public void testFacadeLoginSuccess() throws ResponseException, DataAccessException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    LoginRecord record=new LoginRecord("username", "password");
    LoginResult result=facade.facadeLogin(new LoginRecord("username", "password"));
    assertNotNull(result);
    assertEquals(record.username(), result.username());
    assertNotEquals(result.message(), "Error: unauthorized");
  }

  @Test
  public void testFacadeLoginInvalidCredentials() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    assertThrows(ResponseException.class, () -> facade.facadeLogin(new LoginRecord("invalidUsername", "invalidPassword")));
  }

  @Test
  public void testFacadeLogoutSuccess() throws ResponseException, DataAccessException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    RegisterResult registerResult=facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    LogoutResult result=facade.facadeLogout(new LogoutRecord(registerResult.authToken()));
    assertNotEquals(result.message(), "Error: unauthorized");
    assertThrows(ResponseException.class, () -> facade.facadeList());
  }

  @Test
  public void testFacadeLogoutInvalidToken() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    assertThrows(ResponseException.class, () -> facade.facadeLogout(new LogoutRecord("invalidAuthToken")));
  }

  @Test
  public void testFacadeRegisterSuccess() throws ResponseException, DataAccessException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);
    facade.facadeClear(new ClearRecord());
    RegisterResult result=facade.facadeRegister(new RegisterRecord("username", "password", "email"));
    assertNotNull(result.authToken());
    assertNotEquals(result.message(), "Error: unauthorized");
  }

  @Test
  public void testFacadeRegisterInvalidUsername() throws ResponseException {
    ServerFacade facade=new ServerFacade("http://localhost:" + port);

    assertThrows(ResponseException.class, () -> facade.facadeRegister(new RegisterRecord("", "password", "email")));
  }

  public static int getRandomIndex(int size) {
    Random random=new Random();
    return random.nextInt(size);
  }

}