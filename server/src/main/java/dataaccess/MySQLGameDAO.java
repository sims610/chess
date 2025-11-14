package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.requestresult.JoinRequest;
import model.requestresult.JoinResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class MySQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE game;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    @Override
    public GameData create(String gameName) throws DataAccessException {
//      Integer gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        if (gameName == null) {
            throw new DataAccessException(401, "Error: unauthorized");
        }
        configureDatabase();
        chess.ChessGame chessGame = new chess.ChessGame();
        Integer gameID = ThreadLocalRandom.current().nextInt(1000, 5000);
        GameData gameData = new GameData(gameID, null, null, gameName, chessGame);
        var statement = "INSERT INTO `game`(`gameID`, `whiteUsername`, `blackUsername`, `gameName`, `game`) VALUES (?, ?, ?, ?, ?);";
        executeUpdate(statement, gameData);
        return gameData;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game;";
        return allGames(statement);
    }

    @Override
    public JoinResult joinGame(JoinRequest joinRequest, String username) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?;";
        GameData gameData = queryGame(statement, joinRequest.gameID());
        GameData updatedGameData;
        if (Objects.equals(joinRequest.playerColor(), "WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException(403, "Error: player already taken");
            }
            updatedGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        } else if (Objects.equals(joinRequest.playerColor(), "BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException(403, "Error: player already taken");
            }
            updatedGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else {
            throw new DataAccessException(400, "Error: tried to join invalid Team Color.");
        }
        deleteGame(gameData.gameID());
        var statement2 = "INSERT INTO `game`(`gameID`, `whiteUsername`, `blackUsername`, `gameName`, `game`) VALUES (?, ?, ?, ?, ?);";
        executeUpdate(statement2, updatedGameData);
        return new JoinResult();
    }

    @Override
    public GameData read(Integer gameID) throws DataAccessException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?;";
        return queryGame(statement, gameID);
    }

    GameData queryGame(String statement, Integer findGameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, findGameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");

                        //Read and deserialize the game JSON
                        var json = rs.getString("game");
                        var game = new Gson().fromJson(json, ChessGame.class);

                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: not working");
        }
        return null;
    }

    void deleteGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM game WHERE gameID=?;")) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    ArrayList<GameData> allGames(String statement) throws DataAccessException {
        var games = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");

                        //Read and deserialize the game JSON
                        var json = rs.getString("game");
                        var game = new Gson().fromJson(json, ChessGame.class);

                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
            return games;
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: not working");
        }
    }

    private int executeUpdate(String statement, GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameData.gameID());
                ps.setString(2, gameData.whiteUsername());
                ps.setString(3, gameData.blackUsername());
                ps.setString(4, gameData.gameName());
                var json = new Gson().toJson(gameData.game());
                ps.setString(5, json);

                ps.executeUpdate();

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `game` (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(225) DEFAULT NULL,
              `blackUsername` varchar(225) DEFAULT NULL,
              `gameName` varchar(225) NOT NULL,
              `game` longtext NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    public void configureDatabase() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Unable to configure database.");
        }
    }
}
