package client;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.requestresult.LoginRequest;
import model.requestresult.LoginResult;
import model.requestresult.RegisterRequest;
import model.requestresult.RegisterResult;
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
}
