package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import model.CreateRequest;
import model.CreateResult;
import model.JoinRequest;
import model.JoinResult;

public class JoinGameHandler {
    private final service.GameService gameService = new service.GameService();

    public String handleRequest(Context ctx, String username, GameDAO gameDAO) throws DataAccessException {
        JoinRequest joinRequest = new Gson().fromJson(ctx.body(), JoinRequest.class);
        if (joinRequest.gameID() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        JoinResult joinResult = gameService.join(joinRequest, username, gameDAO);
        return new Gson().toJson(joinResult);
    }
}
