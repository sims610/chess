package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    ArrayList<AuthData> authDataList = new ArrayList<>();

    @Override
    public void clear() {
        authDataList.clear();
    }

    @Override
    public void logout(AuthData myAuthData) {
        authDataList.remove(myAuthData);
    }

    @Override
    public AuthData read(String authToken) {
        for (AuthData authData : authDataList) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        return null;
    }

    @Override
    public void create(AuthData authData) {
        authDataList.add(authData);
    }
}
