package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;
import model.requestresult.*;

import java.util.ArrayList;

public class GameService {

    public CreateResult create(CreateRequest createRequest, GameDAO gameDAO) throws DataAccessException {
        String gameName = createRequest.gameName();
        if (gameName == null) {
            throw new DataAccessException(400, "bad request");
        }
        GameData gameData = createGame(gameName, gameDAO);
        return new CreateResult(gameData.gameID());
    }

    private GameData createGame(String gameName, GameDAO gameDAO) throws DataAccessException {
        return gameDAO.create(gameName);
    }

    public JoinResult join(JoinRequest joinRequest, String username, GameDAO gameDAO) throws DataAccessException {
        return joinGame(joinRequest, username, gameDAO);
    }

    public ListResult list(ListRequest listRequest, GameDAO gameDAO) throws DataAccessException {
        ArrayList<GameData> gameDataList = listGames(gameDAO);
        return new ListResult(gameDataList);
    }

    private ArrayList<GameData> listGames(GameDAO gameDAO) throws DataAccessException {
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
