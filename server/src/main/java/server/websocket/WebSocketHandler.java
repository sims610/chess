package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import io.javalin.websocket.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import java.io.IOException;

import websocket.messages.NotificationMessage;

import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    int gameID = -1;
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, username);
                case MAKE_MOVE -> make_move(ctx.session, username);
                case LEAVE -> leave(ctx.session, username);
                case RESIGN -> resign(ctx.session, username);
            }
        } catch (DataAccessException ex) {
            System.out.println("oh NO");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authdata = authDAO.read(authToken);
        return authdata.username();
    }

    private void resign(Session session, String username) {
    }

    private void leave(Session session, String username) throws IOException {
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(session, gameID, notification);
        connections.leave(gameID, session);
    }

    private void make_move(Session session, String username) {
    }

    private void connect(Session session, String username) throws IOException {
        connections.join(gameID, session);
        var message = String.format("%s joined the game", username);
        var notification = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(session, gameID, notification);
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
