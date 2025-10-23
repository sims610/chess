package service;

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
//    JoinResult join(JoinRequest) {}
    public ListResult list(ListRequest listRequest, GameDAO gameDAO) {
        ArrayList<GameData> gameDataList = listGames(gameDAO);
        return new ListResult(gameDataList);
    }

    private ArrayList<GameData> listGames(GameDAO gameDAO) {
        return gameDAO.listGames();
    }
}
