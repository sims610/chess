package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLGameDAO;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import java.io.IOException;
import java.util.Objects;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    int gameID = -1;
    private final ConnectionManager connections = new ConnectionManager();
    MySQLGameDAO gameDAO = new MySQLGameDAO();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            gameID = command.getGameID();
            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(ctx.session, username);
                case MAKE_MOVE -> make_move(ctx.session, ctx.message(), username);
                case LEAVE -> leave(ctx.session, username);
                case RESIGN -> resign(ctx.session, username);
            }
        } catch (Exception e) {
            String msg = String.format("Error: %s", e.getMessage());
            var error = new ErrorMessage(ERROR, msg);
            connections.sendToClient(ctx.session, error);
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData authdata = authDAO.read(authToken);
        return authdata.username();
    }

    private void resign(Session session, String username) throws IOException, DataAccessException {
        GameData gameData = getGame(gameID);
        ChessGame game = gameData.game();
        boolean player = false;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            player = true;
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            player = true;
        }
        if (player) {
            if (game.getTeamTurn() != null) {
                game.setTeamTurn(null);
                System.out.println(game.getTeamTurn());
                gameDAO.endGame(gameID, game);
                var message = String.format("%s resigned from the game", username);
                var notification = new NotificationMessage(NOTIFICATION, message);
                connections.broadcast(null, gameID, notification);
            } else {
                String msg = String.format("Error: cannot resign", username);
                var error = new ErrorMessage(ERROR, msg);
                connections.sendToClient(session, error);
            }
        } else {
            String msg = String.format("Error: %s is not a player", username);
            var error = new ErrorMessage(ERROR, msg);
            connections.sendToClient(session, error);
        }
    }

    private void leave(Session session, String username) throws IOException, DataAccessException {
        GameData gameData = getGame(gameID);
        ChessGame game = gameData.game();
        var loadGame = new LoadGameMessage(LOAD_GAME, game);
        boolean player = false;
        boolean white = false;
        if (Objects.equals(gameData.whiteUsername(), username)) {
            player = true;
            white = true;
        } else if (Objects.equals(gameData.blackUsername(), username)) {
            player = true;
        }
        if (player) {
            gameDAO.leaveGame(gameID, white, username);
        }
        var message = String.format("%s left the game", username);
        var notification = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(session, gameID, notification);
        connections.leave(gameID, session);
    }

    private void make_move(Session session, String cmessage, String username) throws InvalidMoveException, DataAccessException, IOException {
        MakeMoveCommand command = new Gson().fromJson(cmessage, MakeMoveCommand.class);
        GameData gameData = getGame(gameID);
        ChessGame game = gameData.game();
        chess.ChessMove move = command.move();
        game.makeMove(move);
        gameDAO.makeMove(gameID, game);
        var message = String.format("%s moved from %s to %s", username, command.move().getStartPosition(), command.move().getEndPosition());
        var notification = new NotificationMessage(NOTIFICATION, message);
        connections.broadcast(session, gameID, notification);
    }

    private GameData getGame(int gameID) throws DataAccessException {
        return gameDAO.read(gameID);
    }

    private void connect(Session session, String username) throws IOException, DataAccessException {
        try {
            GameData gameData = getGame(gameID);
            ChessGame game = gameData.game();
            var loadGame = new LoadGameMessage(LOAD_GAME, game);
            connections.sendToClient(session, loadGame);
            connections.join(gameID, session);
            String role;
            if (Objects.equals(gameData.whiteUsername(), username)) {
                role = "white";
            } else if (Objects.equals(gameData.blackUsername(), username)) {
                role = "black";
            } else {
                role = "observer";
            }
            var message = String.format("%s joined the game as %s", username, role);
            var notification = new NotificationMessage(NOTIFICATION, message);
            connections.broadcast(session, gameID, notification);
        } catch (Exception e) {
            String msg = String.format("Error: %s", e.getMessage());
            var error = new ErrorMessage(ERROR, msg);
            connections.sendToClient(session, error);
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }
}
