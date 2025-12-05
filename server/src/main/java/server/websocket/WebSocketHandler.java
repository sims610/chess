package server.websocket;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.SqlUpdateResult;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLUserDAO;
import io.javalin.websocket.*;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import java.io.IOException;
import websocket.messages.ServerMessage;

import static websocket.commands.UserGameCommand.CommandType.*;

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
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authdata = authDAO.read(authToken);
        return authdata.username();
    }

    private void resign(Session session, String username) {
    }

    private void leave(Session session, String username) {
        var message = String.format("%s left the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, notification);
        connections.leave(gameID, session);
    }

    private void make_move(Session session, String username) {
    }

    private void connect(Session session, String username) {
        connections.join(gameID, session);
        var message = String.format("%s joined the game", username);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(session, notification);
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
