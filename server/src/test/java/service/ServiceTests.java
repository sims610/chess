package service;

import model.*;
import org.junit.jupiter.api.*;
import passoff.model.*;
import dataaccess.*;

import java.util.ArrayList;

public class ServiceTests {

    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;
    private static TestUser newUser;
    private static UserService userService;
    private static GameService gameService;
    private static ClearService clearService;


    @BeforeAll
    public static void init() {
        authDAO = new AuthDAO();
        userDAO = new UserDAO();
        gameDAO = new GameDAO();

        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        userService = new UserService();
        gameService = new GameService();
        clearService = new ClearService();
    }

    @AfterEach
    public void cleanup() {
        authDAO = new AuthDAO();
        userDAO = new UserDAO();
        gameDAO = new GameDAO();
    }


    @Test
    public void RegisterServicePasses() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResult registerResult = userService.register(registerRequest, userDAO, authDAO);
        UserData userData = userDAO.read(newUser.getUsername());
        AuthData authData = authDAO.read(registerResult.authToken());

        Assertions.assertNotNull(userData);
        Assertions.assertNotNull(authData);
        Assertions.assertEquals(userData.username(), newUser.getUsername());
        Assertions.assertEquals(userData.password(), newUser.getPassword());
        Assertions.assertEquals(userData.email(), newUser.getEmail());
    }

    @Test
    public void RegisterAlreadyRegisteredClient() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        userService.register(registerRequest, userDAO, authDAO);
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.register(registerRequest, userDAO, authDAO);
        });
    }

    @Test
    public void LoginServiceSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        userService.register(registerRequest, userDAO, authDAO);
        LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        LoginResult loginResult = userService.login(loginRequest, userDAO, authDAO);
        AuthData authData = authDAO.read(loginResult.authToken());

        Assertions.assertNotNull(authData);
        Assertions.assertEquals(newUser.getUsername(), authData.username());
        Assertions.assertEquals(loginResult.authToken(), authData.authToken());
    }

    @Test
    public void LoginServiceFails() {
        LoginRequest loginRequest = new LoginRequest(newUser.getUsername(), newUser.getPassword());
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.login(loginRequest, userDAO, authDAO);
        });
    }

    @Test
    public void LogoutServiceSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResult registerResult = userService.register(registerRequest, userDAO, authDAO);
        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        userService.logout(logoutRequest, authDAO);
        AuthData authData = authDAO.read(logoutRequest.authToken());

        Assertions.assertNull(authData);
    }

    @Test
    public void LogoutServiceFails() {
        LogoutRequest logoutRequest = new LogoutRequest("fake-auth-token1234");
        Assertions.assertThrows(DataAccessException.class, () -> {
            userService.logout(logoutRequest, authDAO);
        });
    }

    @Test
    public void CreateGameSuccess() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("newGame");
        CreateResult createResult = gameService.create(createRequest, gameDAO);
        ArrayList<GameData> gameDataList = gameDAO.listGames();
        Assertions.assertNotNull(createResult);
        Assertions.assertNotEquals(0, gameDataList.size());
    }

    @Test
    public void CreateGameFails() {
        CreateRequest createRequest = new CreateRequest(null);
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.create(createRequest, gameDAO);
        });
    }

    @Test
    public void ListGamesSuccess() throws DataAccessException {
        CreateRequest createRequest = new CreateRequest("newGame");
        CreateRequest createRequest1 = new CreateRequest("fun");
        CreateRequest createRequest2 = new CreateRequest("newGame3");
        gameService.create(createRequest, gameDAO);
        gameService.create(createRequest1, gameDAO);
        gameService.create(createRequest2, gameDAO);
        ArrayList<GameData> gameDataList = gameDAO.listGames();
        Assertions.assertEquals(3, gameDataList.size());
    }

    @Test
    public void JoinGameSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        userService.register(registerRequest, userDAO, authDAO);
        CreateRequest createRequest = new CreateRequest("newGame");
        CreateResult createResult = gameService.create(createRequest, gameDAO);
        JoinRequest joinRequest = new JoinRequest("WHITE", createResult.gameID(), newUser.getUsername());
        gameService.join(joinRequest, newUser.getUsername(), gameDAO);
        GameData gameData = gameDAO.read(createResult.gameID());

        Assertions.assertEquals("NewUser", gameData.whiteUsername());
    }

    @Test
    public void joinGameFails() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        userService.register(registerRequest, userDAO, authDAO);
        CreateRequest createRequest = new CreateRequest("newGame");
        gameService.create(createRequest, gameDAO);
        JoinRequest joinRequest = new JoinRequest("WHITE", null, newUser.getUsername());
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameService.join(joinRequest, newUser.getUsername(), gameDAO);
        });
    }

    @Test
    public void ClearSuccess() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest(newUser.getUsername(), newUser.getPassword(), newUser.getEmail());
        RegisterResult registerResult = userService.register(registerRequest, userDAO, authDAO);
        CreateRequest createRequest = new CreateRequest("newGame");
        gameService.create(createRequest, gameDAO);
        clearService.clear(userDAO, authDAO, gameDAO);

        Assertions.assertNull(userDAO.read(newUser.getUsername()));
        Assertions.assertNull(authDAO.read(registerResult.authToken()));
        Assertions.assertEquals(0, gameDAO.listGames().size());
    }
}
