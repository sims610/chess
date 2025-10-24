package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import model.requestresult.CreateRequest;
import model.requestresult.CreateResult;

public class CreateGameHandler {
    private final service.GameService gameService = new service.GameService();

    public String handleRequest(Context ctx, GameDAO gameDAO) throws DataAccessException {
        CreateRequest createRequest = new Gson().fromJson(ctx.body(), CreateRequest.class);
        if (createRequest.gameName() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        CreateResult createResult = gameService.create(createRequest, gameDAO);
        return new Gson().toJson(createResult);
    }
}
