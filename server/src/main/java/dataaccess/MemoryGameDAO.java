package dataaccess;

import model.GameData;
import model.requestresult.JoinRequest;
import model.requestresult.JoinResult;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void clear() {
        gameDataList.clear();
    }

    @Override
    public GameData create(String gameName) {
        chess.ChessGame chessGame = new chess.ChessGame();
        Integer gameID = ThreadLocalRandom.current().nextInt(1000, 5000);
        GameData gameData = new GameData(gameID, null, null, gameName, chessGame);
        gameDataList.add(gameData);
        return gameData;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return gameDataList;
    }

    @Override
    public JoinResult joinGame(JoinRequest joinRequest, String username) throws DataAccessException {
        for (GameData gameData : gameDataList) {
            if (gameData.gameID().equals(joinRequest.gameID())) {
                GameData updatedGameData;
                if (Objects.equals(joinRequest.playerColor(), "WHITE")) {
                    if (gameData.whiteUsername() != null) {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                    updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
                } else if (Objects.equals(joinRequest.playerColor(), "BLACK")) {
                    if (gameData.blackUsername() != null) {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                    updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
                } else {
                    throw new DataAccessException(400, "Error: tried to join invalid Team Color.");
                }
                gameDataList.remove(gameData);
                gameDataList.add(updatedGameData);
                return new JoinResult();
            }
        }
        return null;
    }

    @Override
    public GameData read(Integer gameID) {
        for (GameData gameData : gameDataList) {
            if (gameData.gameID().equals(gameID)) {
                return gameData;
            }
        }
        return null;
    }
}
