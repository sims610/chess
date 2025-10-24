package dataaccess;

import model.GameData;
import model.requestresult.JoinRequest;
import model.requestresult.JoinResult;

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

    public JoinResult joinGame(JoinRequest joinRequest, String username) throws DataAccessException {
        return memoryGameDAO.joinGame(joinRequest, username);
    }

    public GameData read(Integer gameID) {
        return memoryGameDAO.read(gameID);
    }
}
