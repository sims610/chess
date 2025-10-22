package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO {
    ArrayList<AuthData> authDataList = new ArrayList<>();

    public void delete() {
        authDataList.clear();
    }
}
