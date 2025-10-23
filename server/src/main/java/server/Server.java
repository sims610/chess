package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    public final RegisterHandler registerHandler = new RegisterHandler();
    private final ClearHandler clearHandler = new ClearHandler();
    private final LoginHandler loginHandler = new LoginHandler();
    private final LogoutHandler logoutHandler = new LogoutHandler();
    private final AuthDAO authDAO = new AuthDAO();
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this::logoutUser);
        javalin.delete("/db", this::clear);
        javalin.exception(DataAccessException.class, this::exceptionHandler);

    }

    private void logoutUser(Context ctx) throws DataAccessException {
        if (authorized(ctx)) {
            String authToken = ctx.header("Authorized");
            AuthData authData = authDAO.read(authToken);
            authDAO.logout(authData);
        }
//        String logout = logoutHandler.handleRequest(ctx, authDAO);
        ctx.status(200);
    }

    private void loginUser(Context ctx) throws DataAccessException {
        String login = loginHandler.handleRequest(ctx, userDAO, authDAO);
        ctx.json(login);
    }

    private void registerUser(Context ctx) throws DataAccessException {
        String register = registerHandler.handleRequest(ctx, userDAO, authDAO);
        ctx.json(register);
    }

    private void clear(Context ctx) throws DataAccessException {
        String clear = clearHandler.handleRequest(userDAO, authDAO, gameDAO);
        ctx.json(clear);
    }

    private void exceptionHandler(DataAccessException ex, Context ctx) {
        ctx.status(ex.httpStatus());
        ctx.json(ex.toJson());
    }

    private boolean authorized(Context ctx) {
        String authToken = ctx.header("Authorization");
        if (authDAO.read(authToken) == null) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("msg", "invalid authorization")));
            return false;
        }
        return true;
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
