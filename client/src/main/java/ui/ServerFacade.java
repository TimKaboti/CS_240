package ui;

import Result.*;
import model.*;

public class ServerFacade {
    ClientCommunicator communicator = new ClientCommunicator();


    private ClearResult facadeClear(ClearRecord record) throws ResponseException {
        Class<ClearResult> result = ClearResult.class; // Get the class type of ClearResult
        return communicator.deleteRequest("/db", record, result);
    }

    public CreateGameResult facadeCreate(CreateGameRecord record) throws ResponseException {
        Class<CreateGameResult> result = CreateGameResult.class; // Get the class type of ClearResult
        return communicator.postRequest("/game", record, result);
    } //post

    public JoinGameResult facadeJoin(JoinGameRecord record) throws ResponseException {
        Class<JoinGameResult> result = JoinGameResult.class; // Get the class type of ClearResult
        return communicator.putRequest("/game", record, result);
    } //put

    public ListGamesResult facadeList(ListGameRecord record) throws ResponseException {
        Class<ListGamesResult> result = ListGamesResult.class; // Get the class type of ClearResult
        return communicator.getRequest("/game", record, result);
    } //get

    public LoginResult facadeLogin(LoginRecord record) throws ResponseException {
        Class<LoginResult> result = LoginResult.class; // Get the class type of ClearResult
        return communicator.postRequest("/session", record, result);
    } //post

    public LogoutResult facadeLogout(LogoutRecord record) throws ResponseException {
        Class<LogoutResult> result = LogoutResult.class; // Get the class type of ClearResult
        return communicator.deleteRequest("/session", record, result);
    } //delete

    public RegisterResult facadeRegister(RegisterRecord record) throws ResponseException {
        Class<RegisterResult> result = RegisterResult.class; // Get the class type of ClearResult
        return communicator.postRequest("/user", record, result);
    } //post

}
