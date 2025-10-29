package dataaccess;

import model.GameData;
import model.requestresult.JoinRequest;
import model.requestresult.JoinResult;

import java.util.ArrayList;

public interface GameDAO {

    void clear();

    GameData create(String gameName);

    ArrayList<GameData> listGames();

    JoinResult joinGame(JoinRequest joinRequest, String username) throws DataAccessException;

    GameData read(Integer gameID);
}
