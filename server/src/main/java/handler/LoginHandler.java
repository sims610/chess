package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.RequestResult.LoginRequest;
import model.RequestResult.LoginResult;

public class LoginHandler {
    private final service.UserService userService = new service.UserService();

    public String handleRequest(Context ctx, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        LoginRequest login = new Gson().fromJson(ctx.body(), LoginRequest.class);
        if (login.password() == null || login.username() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }

        LoginResult loginResult = userService.login(login, userDAO, authDAO);
        return new Gson().toJson(loginResult);
    }
}
