package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;

    void logout(AuthData authData) throws DataAccessException;

    AuthData read(String authToken) throws DataAccessException;

    void create(AuthData authData) throws DataAccessException;
}
