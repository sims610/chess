package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO {
    ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public UserData read(String username, String password) throws DataAccessException {
        for (UserData userData : userDataList) {
            if (userData.username().equals(username)) {
                if (userData.password().equals(password)) {
                    return userData;
                } else {
                    throw new DataAccessException(401, "Error: unauthorized");
                }
            }
        }
        return null;
    }

    @Override
    public void create(UserData userData) throws DataAccessException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        userDataList.add(userData);
    }

    @Override
    public void clear() {
        userDataList.clear();
    }
}
