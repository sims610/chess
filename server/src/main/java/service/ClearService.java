package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.RegisterHandler;
import model.ClearResult;

public class ClearService {
    private static UserDAO userDAO = new UserDAO();
    private static GameDAO gameDAO = new GameDAO();
    private static AuthDAO authDAO = new AuthDAO();

    public ClearResult clear(RegisterHandler registerHandler) throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
        return new ClearResult();
    }
}
