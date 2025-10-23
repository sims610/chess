package dataaccess;

import model.AuthData;

public class AuthDAO {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    public void clear() {
        memoryAuthDAO.delete();
    }

    public void logout(AuthData authData) {
        memoryAuthDAO.logout(authData);
    }

    public AuthData read(String authToken) {
        return memoryAuthDAO.read(authToken);
    }

    public void create(AuthData authData) {
        memoryAuthDAO.create(authData);
    }
}
