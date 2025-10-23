package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

import java.util.ArrayList;

public class GameService {

    public CreateResult create(CreateRequest createRequest, GameDAO gameDAO) {
        String gameName = createRequest.gameName();
        GameData gameData = createGame(gameName, gameDAO);
        return new CreateResult(gameData.gameID());
    }

    private GameData createGame(String gameName, GameDAO gameDAO) {
        return gameDAO.create(gameName);
    }

    public JoinResult join(JoinRequest joinRequest, String username, GameDAO gameDAO) throws DataAccessException {
        return joinGame(joinRequest, username, gameDAO);
    }

    public ListResult list(ListRequest listRequest, GameDAO gameDAO) {
        ArrayList<GameData> gameDataList = listGames(gameDAO);
        return new ListResult(gameDataList);
    }

    private ArrayList<GameData> listGames(GameDAO gameDAO) {
        return gameDAO.listGames();
    }

    private JoinResult joinGame(JoinRequest joinRequest, String username, GameDAO gameDAO) throws DataAccessException {
        JoinResult joinresult =  gameDAO.joinGame(joinRequest, username);
        if (joinresult == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        return joinresult;
    }
}
