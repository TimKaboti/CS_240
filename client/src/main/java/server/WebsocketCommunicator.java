package server;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator {
  public Session session;


  public void WebsocketCommunicator() throws Exception {
    URI uri = new URI("ws://localhost:8080/connect");
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);
    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      public void onMessage(String message) {
        System.out.println(message);
//        here ive got a message. what I need to do is figure out what methods need to be called as a result of this
//        message. for example LOAD_GAME; if this were the message, I'd need to redraw the board for the player.
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

