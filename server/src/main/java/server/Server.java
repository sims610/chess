package server;

import com.google.gson.Gson;
import dataaccess.*;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;
    public final RegisterHandler registerHandler = new RegisterHandler();
    private final ClearHandler clearHandler = new ClearHandler();
    private final AuthDAO authDAO = new AuthDAO();
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerUser);
        javalin.delete("/db", this::clear);
        javalin.exception(DataAccessException.class, this::exceptionHandler);

    }

    private void registerUser(Context ctx) throws DataAccessException {
        String register = registerHandler.handleRequest(ctx, userDAO);
        ctx.json(register);
    }

    private void clear(Context ctx) throws DataAccessException {
        String clear = clearHandler.handleRequest(userDAO, authDAO, gameDAO);
        ctx.json(clear);
    }

    private void exceptionHandler(DataAccessException ex, Context ctx) {
        ctx.status(403);
        ctx.json(ex.toJson());
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
