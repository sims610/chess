package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;

import model.requestresult.RegisterRequest;
import model.requestresult.RegisterResult;

public class RegisterHandler {
    private final service.UserService userService = new service.UserService();

    public String handleRequest(Context ctx, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        RegisterRequest user = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult userResult = userService.register(user, userDAO, authDAO);
        return new Gson().toJson(userResult);
    }
}
