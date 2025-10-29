package dataaccess;

import model.*;

public interface UserDAO {

    UserData read(String username);

    void create(UserData userData) throws DataAccessException;

    void clear();
}
