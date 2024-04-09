package server;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.Error;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator {
  public Session session;
  NotificationHandler notificationHandler;

  public void WebsocketCommunicator(NotificationHandler handler) throws Exception {
    this.notificationHandler = handler;
    URI uri = new URI("ws://localhost:8080/connect");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);
    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      public void onMessage(String message) {
        System.out.println(message);
        ServerMessage newMessage = new Gson().fromJson(message, ServerMessage.class);
        switch(newMessage.getServerMessageType()){
          case LOAD_GAME:
            LoadGame load = new Gson().fromJson(message, LoadGame.class);
            notificationHandler.notify(load);
            break;
          case NOTIFICATION:
            Notification notification = new Gson().fromJson(message, Notification.class);
            notificationHandler.notify(notification);
            break;
          case ERROR:
            Error error = new Gson().fromJson(message, Error.class);
            notificationHandler.notify(error);
            break;
        }

//        what I need to do is figure out what methods need to be called as a result of each
//        message. for example LOAD_GAME; if this were the message, I'd need to redraw the board for the players & observers.
      }
    });
  }

//  public static void main(String[] args) throws Exception {
//    var ws = new WSClient();
//    Scanner scanner = new Scanner(System.in);

//    System.out.println("Enter a message you want to echo");
//    while (true) {
//      ws.send(scanner.nextLine());
//    }
//  }
    public void send(String msg) throws Exception {
      this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}

