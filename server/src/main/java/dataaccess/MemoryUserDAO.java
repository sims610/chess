package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO {
    ArrayList<UserData> userDataList = new ArrayList<>();

    public UserData read(String username) {
        for (UserData userData : userDataList) {
            if (userData.username().equals(username)) {
                return userData;
            }
        }
        return null;
    }

    public void create(UserData userData) throws DataAccessException {
        if (userData.username() == null || userData.password() == null || userData.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        userDataList.add(userData);
    }

    public void delete() {
        userDataList.clear();
    }
}
