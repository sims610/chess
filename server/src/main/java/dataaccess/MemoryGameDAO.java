package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MemoryGameDAO {
    ArrayList<GameData> gameDataList = new ArrayList<>();

    public void delete() {
        gameDataList.clear();
    }

    public GameData create(String gameName) {
        chess.ChessGame chessGame = new chess.ChessGame();
        Integer gameID = ThreadLocalRandom.current().nextInt(1000, 5000);
        GameData gameData = new GameData(gameID, "", "", gameName, chessGame);
        gameDataList.add(gameData);
        return gameData;
    }
}
