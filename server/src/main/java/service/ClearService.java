package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import handler.RegisterHandler;
import model.ClearResult;

public class ClearService {

    public ClearResult clear(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
        return new ClearResult();
    }
}
