package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.UseProxyResponse;
import model.*;

import java.util.UUID;

public class UserService {

    public RegisterResult register(RegisterRequest registerRequest, UserDAO userDAO) throws DataAccessException {
        if (getUser(registerRequest.username(), userDAO) == null) {
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            createUser(userData, userDAO);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.username());
            createAuth(authData);
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new DataAccessException("Error: username already taken");
        }
    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}

    private UserData getUser(String username, UserDAO userDAO) {
        return userDAO.read(username);
    }

    private void createUser(UserData userData, UserDAO userDAO) {
        userDAO.create(userData);
    }

    private void createAuth(AuthData authData) {
//        AuthDAO.create(authData);
    }
}
