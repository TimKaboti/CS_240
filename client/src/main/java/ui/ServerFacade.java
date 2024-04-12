package ui;

import Result.*;
import com.google.gson.Gson;
import model.*;
import server.WebsocketCommunicator;
import webSocketMessages.userCommands.*;

public class ServerFacade {

  public ServerFacade(String url) {
    this.communicator=new ClientCommunicator(url);
  }

  ClientCommunicator communicator;

  WebsocketCommunicator socket;

  String authToken=null;


  public ClearResult facadeClear(ClearRecord record) throws ResponseException {
    Class<ClearResult> result=ClearResult.class; // Get the class type
    return communicator.deleteRequest("/db", record, result, null);
  }

  public CreateGameResult facadeCreate(CreateGameRecord record) throws ResponseException {
    Class<CreateGameResult> result=CreateGameResult.class; // Get the class type
    return communicator.postRequest("/game", record, result, authToken);
  } //post

  public JoinGameResult facadeJoin(JoinGameRecord record) throws ResponseException {
    Class<JoinGameResult> result=JoinGameResult.class; // Get the class type
    return communicator.putRequest("/game", record, result, authToken);
  } //put

  public ListGamesResult facadeList() throws ResponseException {
    Class<ListGamesResult> result=ListGamesResult.class; // Get the class type
    return communicator.getRequest("/game", null, result, authToken);
  } //get

  public LoginResult facadeLogin(LoginRecord record) throws ResponseException {
    Class<LoginResult> result=LoginResult.class; // Get the class type
    LoginResult loginResult=communicator.postRequest("/session", record, result, null);
    authToken=loginResult.authToken();
    return loginResult;
  } //post

  public LogoutResult facadeLogout(LogoutRecord record) throws ResponseException {
    Class<LogoutResult> result=LogoutResult.class; // Get the class type
    LogoutResult logoutResult=communicator.deleteRequest("/session", record, result, authToken);
    authToken=null;
    return logoutResult;
  } //delete

  public RegisterResult facadeRegister(RegisterRecord record) throws ResponseException {
    Class<RegisterResult> result=RegisterResult.class; // Get the class type
    RegisterResult registerResult=communicator.postRequest("/user", record, RegisterResult.class, authToken);
    authToken=registerResult.authToken();
    return registerResult;
  } //post

  public void joinPlayer(JoinPlayer join) throws Exception {
    String command = new Gson().toJson(join, UserGameCommand.class);
    socket.send(command);
  }

  public void joinObserver(JoinObserver observer) throws Exception {
    String command = new Gson().toJson(observer, UserGameCommand.class);
    socket.send(command);
  }

  public void makeMove(MakeMove move) throws Exception {
    String command = new Gson().toJson(move, UserGameCommand.class);
    socket.send(command);
  }

  public void leave(Leave leave) throws Exception {
    String command = new Gson().toJson(leave, UserGameCommand.class);
    socket.send(command);
  }

  public void resign(Resign resign) throws Exception {
    String command = new Gson().toJson(resign, UserGameCommand.class);
    socket.send(command);
  }

}
