package dataaccess;

import model.*;

public class UserDAO {
    private final MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

    public UserData read(String username) {
        return memoryUserDAO.read(username);
    }

    public void create(UserData userData) {
        memoryUserDAO.create(userData);
    }

    public void clear() {
        memoryUserDAO.delete();
    }
}
