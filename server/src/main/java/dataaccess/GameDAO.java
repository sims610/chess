package dataaccess;

public class GameDAO {
    private final MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public void clear() {
        memoryGameDAO.delete();
    }
}
