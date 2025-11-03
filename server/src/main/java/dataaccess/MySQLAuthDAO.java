package dataaccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO{

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE auth;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    @Override
    public void logout(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?;";
        deleteAuthToken(statement, authData.authToken());
    }

    @Override
    public AuthData read(String authToken) throws DataAccessException {
        var statement = "SELECT authToken, username FROM auth WHERE authToken=?;";
        return queryAuth(statement, authToken);
    }

    @Override
    public void create(AuthData authData) throws DataAccessException {
        configureDatabase();
        var statement = "INSERT INTO `chess`.`auth`(`authToken`, `username`) VALUES (?, ?);";
        executeUpdate(statement, authData.authToken(), authData.username());
    }

    void deleteAuthToken(String statement, String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    AuthData queryAuth(String statement, String findAuthToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, findAuthToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var authToken = rs.getString("authToken");
                        var username = rs.getString("username");

                        return new AuthData(authToken, username);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: can't access database");
        }
        return null;
    }

    private int executeUpdate(String statement, String authToken, String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                ps.setString(2, username);

                ps.executeUpdate();

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, "Error: Can't access database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `auth` (
              `authToken` varchar(225) NOT NULL,
              `username` varchar(45) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDatabase() throws DataAccessException {
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
