package service;

import dataaccess.GameDAO;
import model.CreateRequest;
import model.CreateResult;
import model.GameData;

public class GameService {

    public CreateResult create(CreateRequest createRequest, GameDAO gameDAO) {
        String gameName = createRequest.gameName();
        GameData gameData = createGame(gameName, gameDAO);
        return new CreateResult(gameData.gameID());
    }

    private GameData createGame(String gameName, GameDAO gameDAO) {
        return gameDAO.create(gameName);
    }
//    JoinResult join(JoinRequest) {}
//    ListResult list(ListRequest) {}
}
