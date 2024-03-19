package ui;

import Result.*;
import model.*;

public class ServerFacade {

    public ServerFacade(String url) {
        this.communicator = new ClientCommunicator(url);
    }

    ClientCommunicator communicator;

    String authToken = null;


    private ClearResult facadeClear(ClearRecord record) throws ResponseException {
        Class<ClearResult> result = ClearResult.class; // Get the class type
        return communicator.deleteRequest("/db", record, result, null);
    }

    public CreateGameResult facadeCreate(CreateGameRecord record) throws ResponseException {
        Class<CreateGameResult> result = CreateGameResult.class; // Get the class type
        return communicator.postRequest("/game", record, result, authToken);
    } //post

    public JoinGameResult facadeJoin(JoinGameRecord record) throws ResponseException {
        Class<JoinGameResult> result = JoinGameResult.class; // Get the class type
        return communicator.putRequest("/game", record, result, authToken);
    } //put

    public ListGamesResult facadeList(ListGameRecord record) throws ResponseException {
        Class<ListGamesResult> result = ListGamesResult.class; // Get the class type
        return communicator.getRequest("/game", record, result, authToken);
    } //get

    public LoginResult facadeLogin(LoginRecord record) throws ResponseException {
        Class<LoginResult> result = LoginResult.class; // Get the class type
        LoginResult loginResult = communicator.postRequest("/session", record, result, null);
        authToken = loginResult.authToken();
        return loginResult;

    } //post

    public LogoutResult facadeLogout(LogoutRecord record) throws ResponseException {
        Class<LogoutResult> result = LogoutResult.class; // Get the class type
        return communicator.deleteRequest("/session", record, result, authToken);
    } //delete

    public RegisterResult facadeRegister(RegisterRecord record) throws ResponseException {
        Class<RegisterResult> result = RegisterResult.class; // Get the class type
        RegisterResult registerResult = communicator.postRequest("/user", record, result, authToken);
        authToken = registerResult.authToken();
        return registerResult;
    } //post

}
