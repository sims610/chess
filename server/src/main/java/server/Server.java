package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import handler.*;
import io.javalin.*;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;
    private final RegisterHandler registerHandler = new RegisterHandler();
    private final ClearHandler clearHandler = new ClearHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", this::registerUser);
        javalin.delete("/db", this::clear);
        javalin.exception(DataAccessException.class, this::exceptionHandler);

    }

    private void registerUser(Context ctx) throws DataAccessException {
        String register = registerHandler.handleRequest(ctx);
        ctx.json(register);
    }

    private void clear(Context ctx) throws DataAccessException {
        String clear = clearHandler.handleRequest();
        ctx.json(clear);
    }

    private void exceptionHandler(DataAccessException ex, Context ctx) {
        ctx.status(403);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
