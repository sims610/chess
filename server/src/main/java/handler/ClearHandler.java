package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.ClearResult;
import model.RegisterRequest;
import model.RegisterResult;
import service.ClearService;

public class ClearHandler {
    private final service.ClearService clearService = new service.ClearService();

    public String handleRequest(RegisterHandler registerHandler) throws DataAccessException {
        ClearResult clearResult = clearService.clear(registerHandler);
        return new Gson().toJson(clearResult);
    }
}
