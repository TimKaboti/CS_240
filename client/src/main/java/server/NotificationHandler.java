package server;

import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {

  public void notify(String message);
}
