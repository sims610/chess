package client;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requestresult.*;
import org.junit.jupiter.api.*;
import server.Server;

import javax.xml.crypto.Data;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static RegisterRequest newUser;
    private static LoginRequest loginUser;
    private static CreateRequest createGame;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", port);
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        userDAO = new MySQLUserDAO();
        newUser = new RegisterRequest("newUser", "newUserPassword", "newUserEmail");
        loginUser = new LoginRequest("newUser", "newUserPassword");
        createGame = new CreateRequest("newGame");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDB() throws DataAccessException {
        authDAO.clear();
        gameDAO.clear();
        userDAO.clear();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerPassesTest() {
        RegisterResult result = serverFacade.register(newUser);
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void registerFailsTest() {
        // Tries to register a user twice
        serverFacade.register(newUser);
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.register(newUser);
        });
    }

    @Test
    public void loginPassesTest() throws DataAccessException {
        serverFacade.register(newUser);
        LoginResult result = serverFacade.login(loginUser);
        AuthData expect = authDAO.read(result.authToken());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertEquals(expect.username(), result.username());
    }

    @Test
    public void loginFailsTest() {
        // Tries to login a user that is not registered
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.login(loginUser);
        });
    }

    @Test
    public void logoutPassesTest() throws DataAccessException {
        RegisterResult registerResult = serverFacade.register(newUser);
        LogoutRequest logoutUser = new LogoutRequest(registerResult.authToken());
        LogoutResult result = serverFacade.logout(logoutUser);
    }

    @Test
    public void logoutFailsTest() {
        // Tries to logout twice
        RegisterResult registerResult = serverFacade.register(newUser);
        LogoutRequest logoutUser = new LogoutRequest(registerResult.authToken());
        serverFacade.logout(logoutUser);
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.logout(logoutUser);
        });
    }

    @Test
    public void createPassesTest() {
        serverFacade.register(newUser);
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.create(createGame);
        });
    }

    @Test
    public void createFailsTest() {
        serverFacade.register(newUser);
        CreateRequest createGame = null;
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.create(createGame);
        });
    }

    @Test
    public void listGamesPassesTest() {
        serverFacade.register(newUser);
        serverFacade.create(createGame);
        ListResult result = serverFacade.listGames(new ListRequest());
        Assertions.assertEquals(1, result.games().size());
    }

    @Test
    public void listGameFailsTest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.listGames(new ListRequest());
        });
    }

    @Test
    public void joinGamePassesTest() throws DataAccessException {
        serverFacade.register(newUser);
        CreateResult create = serverFacade.create(createGame);
        serverFacade.joinGame(new JoinRequest("BLACK", create.gameID(), newUser.username()));
        GameData expected = gameDAO.read(create.gameID());
        Assertions.assertEquals(newUser.username(), expected.blackUsername());
    }

    @Test
    public void joinGameFailsTest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            serverFacade.joinGame(new JoinRequest("WHITE", 1234, newUser.username()));
        });
    }
}
