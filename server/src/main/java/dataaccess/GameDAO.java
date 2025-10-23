package dataaccess;

import model.GameData;

public class GameDAO {
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public void clear() {
        memoryGameDAO.delete();
    }

    public GameData create(String gameName) {
        return memoryGameDAO.create(gameName);
    }
}
