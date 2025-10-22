package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO {
    ArrayList<GameData> gameDataList = new ArrayList<>();

    public void delete() {
        gameDataList.clear();
    }
}
