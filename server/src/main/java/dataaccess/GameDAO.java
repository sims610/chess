package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GameDAO {
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public void clear() {
        memoryGameDAO.delete();
    }

    public GameData create(String gameName) {
        return memoryGameDAO.create(gameName);
    }

    public ArrayList<GameData> listGames() {
        return memoryGameDAO.listGames();
    }
}
