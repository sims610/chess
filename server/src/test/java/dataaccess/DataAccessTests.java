package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requestresult.JoinRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static dataaccess.DatabaseManager.createDatabase;

public class DataAccessTests {

    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static UserData newUser;
    private static AuthData newAuth;
    private static GameData newGame;

    @BeforeAll
    public static void init() {
        userDAO = new MySQLUserDAO();
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        newUser = new UserData("newUser", "newPassword", "email@eu.com");
        newAuth = new AuthData("hjwq-1234-qwer-9078", "newUser");
        newGame = new GameData(1234, null, null, "newGame", new ChessGame());
        try {
            createDatabase();
            authDAO.clear();
        } catch (DataAccessException ex) {
            System.out.println("Error: Cannot create Database");
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

//MySQLAuthTests
    @Test
    public void sqlAuthClearPasses() throws DataAccessException {
        Assertions.assertDoesNotThrow(() -> authDAO.clear());
    }

    @Test
    public void sqlAuthLogoutPasses() throws DataAccessException {
        authDAO.create(newAuth);
        authDAO.logout(newAuth);
        AuthData myAuth = authDAO.read(newAuth.authToken());
        Assertions.assertNull(myAuth);
    }

    @Test
    public void sqlAuthLogoutFails() {
        Assertions.assertDoesNotThrow(() -> authDAO.logout(newAuth));
    }

    @Test
    public void sqlAuthReadPasses() throws DataAccessException {
        authDAO.create(newAuth);
        AuthData readAuth = authDAO.read(newAuth.authToken());
        Assertions.assertEquals(newAuth.authToken(), readAuth.authToken());
        Assertions.assertEquals(newAuth.username(), readAuth.username());
    }

    @Test
    public void sqlAuthReadFails() throws DataAccessException {
        AuthData myData = authDAO.read(null);
        Assertions.assertNull(myData);
    }

    @Test
    public void sqlAuthCreatePasses() throws DataAccessException {
        authDAO.create(newAuth);
        AuthData myAuth = authDAO.read(newAuth.authToken());
        Assertions.assertEquals(newAuth, myAuth);
    }

    @Test
    public void sqlAuthCreateFails() throws DataAccessException {
        authDAO.create(newAuth);
        AuthData myAuth = authDAO.read("invalid-auth-token");
        Assertions.assertNull(myAuth);
    }

//MySQLUserTests
    @Test
    public void sqlUserReadPasses() throws DataAccessException {
        userDAO.create(newUser);
        UserData findUser = userDAO.read(newUser.username(), newUser.password());
        Assertions.assertEquals(findUser.username(), newUser.username());
        Assertions.assertEquals(findUser.email(), newUser.email());
    }

    @Test
    public void sqlUserReadFails() throws DataAccessException {
        userDAO.create(newUser);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.read(newUser.username(), "InvalidPassword");
        });

    }

    @Test
    public void sqlUserCreatePasses() throws DataAccessException {
        userDAO.create(newUser);
        UserData myUser = userDAO.read(newUser.username(), newUser.password());
        Assertions.assertEquals(myUser.username(), newUser.username());
        Assertions.assertEquals(myUser.email(), newUser.email());
    }

    @Test
    public void sqlUserCreateFails() {
        UserData invalidUser = new UserData(null, "password", "email");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userDAO.create(invalidUser);
        });
    }

    @Test
    public void sqlUserClearPasses() {
        Assertions.assertDoesNotThrow(() -> userDAO.clear());
    }

//MySQLGameTests
    @Test
    public void sqlGameClearPasses() {
        Assertions.assertDoesNotThrow(() -> gameDAO.clear());
    }

    @Test
    public void sqlGameCreatePasses() throws DataAccessException {
        GameData myGame = gameDAO.create(newGame.gameName());
        GameData gameData = gameDAO.read(myGame.gameID());
        Assertions.assertEquals(myGame, gameData);
    }

    @Test
    public void sqlGameCreateFails() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.create(null);
        });
    }

    @Test
    public void sqlGameListGamesPasses() throws DataAccessException {
        gameDAO.create(newGame.gameName());
        ArrayList<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(1, games.size());
    }

    @Test
    public void sqlGameJoinGamePasses() throws DataAccessException {
        GameData myGame = gameDAO.create(newGame.gameName());
        userDAO.create(newUser);
        JoinRequest myRequest = new JoinRequest("WHITE", myGame.gameID(), newUser.username());
        Assertions.assertDoesNotThrow(() -> gameDAO.joinGame(myRequest, newUser.username()));
    }

    @Test
    public void sqlGameJoinGameFails() throws DataAccessException {
        GameData myGame = gameDAO.create(newGame.gameName());
        userDAO.create(newUser);
        JoinRequest myRequest = new JoinRequest("GREEN", myGame.gameID(), newUser.username());
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.joinGame(myRequest, newUser.username());
        });
    }

    @Test
    public void sqlGameReadPasses() throws DataAccessException {
        GameData myGame = gameDAO.create(newGame.gameName());
        GameData readGame = gameDAO.read(myGame.gameID());
        Assertions.assertEquals(myGame, readGame);
    }

    @Test
    public void sqlGameReadFails() throws DataAccessException {
        GameData readGame = gameDAO.read(newGame.gameID());
        Assertions.assertNull(readGame);
    }
}
