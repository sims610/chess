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

    public void create(UserData userData) {
        userDataList.add(userData);
    }

    public void delete() {
        userDataList.clear();
    }
}
