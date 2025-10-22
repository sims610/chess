package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;

import model.*;
import java.util.Map;

public class RegisterHandler {
    private final service.UserService userService = new service.UserService();

    public String handleRequest(Context ctx) throws DataAccessException {
        RegisterRequest user = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult userResult = userService.register(user);
        return new Gson().toJson(userResult);
    }
}
