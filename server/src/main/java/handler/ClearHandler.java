package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.*;
import model.requestresult.ClearResult;

public class ClearHandler {
    private final service.ClearService clearService = new service.ClearService();

    public String handleRequest(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        ClearResult clearResult = clearService.clear(userDAO, authDAO, gameDAO);
        return new Gson().toJson(clearResult);
    }
}
