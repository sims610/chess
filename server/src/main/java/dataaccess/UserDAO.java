package dataaccess;

import model.*;

public interface UserDAO {

    UserData read(String username) throws DataAccessException;

    void create(UserData userData) throws DataAccessException;

    void clear() throws DataAccessException;
}
