package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryAuthDAO {
    ArrayList<AuthData> authDataList = new ArrayList<>();

    public void delete() {
        authDataList.clear();
    }

    public void logout(AuthData myAuthData) {
        authDataList.remove(myAuthData);
    }

    public AuthData read(String authToken) {
        for (AuthData authData : authDataList) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        return null;
    }

    public void create(AuthData authData) {
        authDataList.add(authData);
    }
}
