package dataaccess;

public class AuthDAO {
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    public void clear() {
        memoryAuthDAO.delete();
    }
}
