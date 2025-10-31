package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import model.requestresult.*;

import java.util.UUID;

public class UserService {

    public RegisterResult register(RegisterRequest registerRequest, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        if (getUser(registerRequest.username(), userDAO) == null) {
            if (registerRequest.password() == null) {
                throw new DataAccessException(400, "Error: bad request");
            }
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            createUser(userData, userDAO);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.username());
            createAuth(authData, authDAO);
            return new RegisterResult(registerRequest.username(), authToken);
        } else {
            throw new DataAccessException(403, "Error: already taken");
        }
    }

    public LoginResult login(LoginRequest loginRequest, UserDAO userDAO, AuthDAO authDAO) throws DataAccessException {
        UserData userData = getUser(loginRequest.username(), userDAO);
        if (userData != null) {
            if (!userData.password().equals(loginRequest.password())) {
                throw new DataAccessException(401, "Error: unauthorized");
            }
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, userData.username());
            createAuth(authData, authDAO);
            return new LoginResult(userData.username(), authToken);
        }
        throw new DataAccessException(401, "Error: bad request");
    }

    public LogoutResult logout(LogoutRequest logoutRequest, AuthDAO authDAO) throws DataAccessException {
        AuthData authData = authDAO.read(logoutRequest.authToken());
        if (authData == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        deleteAuth(authData, authDAO);
        return new LogoutResult();
    }

    private void deleteAuth(AuthData authData, AuthDAO authDAO) throws DataAccessException {
        authDAO.logout(authData);
    }

    private UserData getUser(String username, UserDAO userDAO) {
        return userDAO.read(username);
    }

    private void createUser(UserData userData, UserDAO userDAO) throws DataAccessException {
        userDAO.create(userData);
    }

    private void createAuth(AuthData authData, AuthDAO authDAO) throws DataAccessException {
        authDAO.create(authData);
    }
}
