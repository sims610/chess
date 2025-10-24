package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.http.Context;
import model.requestresult.ListRequest;
import model.requestresult.ListResult;

public class ListGameHandler {
    private final service.GameService gameService = new service.GameService();

    public String handleRequest(Context ctx, GameDAO gameDAO) throws DataAccessException {
        ListRequest listRequest = new Gson().fromJson(ctx.body(), ListRequest.class);
        ListResult listResult = gameService.list(listRequest, gameDAO);
        return new Gson().toJson(listResult);
    }
}
