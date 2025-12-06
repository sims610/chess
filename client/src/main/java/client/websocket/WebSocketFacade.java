package client.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;

import static websocket.commands.UserGameCommand.CommandType.*;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    String authToken;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {

                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    NotificationMessage thing = null;
                    switch (notification.getServerMessageType()) {
                        case LOAD_GAME -> {
                        }
                        case ERROR -> {
                        }
                        case NOTIFICATION -> {
                            thing = new Gson().fromJson(message, NotificationMessage.class);
                        }
                    }
                    notificationHandler.notify(thing);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void connect(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse");
        }
    }

    public void leave(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse");
        }
    }

    public void move(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse");
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            var command = new UserGameCommand(RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException("Could not parse");
        }
    }

    public void highlight() {}
}
