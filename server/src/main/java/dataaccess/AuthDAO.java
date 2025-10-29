package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();

    void logout(AuthData authData);

    AuthData read(String authToken);

    void create(AuthData authData);
}
