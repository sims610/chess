package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData read(String username) throws DataAccessException {
        configureDatabase();
        var statement = "SELECT username, password, email FROM user WHERE username=?;";
        return queryUser(statement, username);
    }

    @Override
    public void create(UserData userData) throws DataAccessException {
        configureDatabase();
        var statement = "INSERT INTO `chess`.`user`(`username`, `password`, `email`) VALUES (?, ?, ?);";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE user;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Doesn't work");
        }
    }

    UserData queryUser(String statement, String findUsername) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, findUsername);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");

                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: not working");
        }
        return null;
    }

    private int executeUpdate(String statement, String username, String password, String email) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);

                ps.executeUpdate();

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(500, "Didn't work");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS `user` (
              `username` varchar(225) NOT NULL,
              `password` varchar(225) NOT NULL,
              `email` varchar(225) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(email),
              INDEX(password),
              INDEX(username)
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
        } catch (DataAccessException ex) {
            throw new DataAccessException(500, "Message: Unable to configure database.");
        } catch (SQLException ex) {
            throw new DataAccessException(500, "message: words");
        }
    }
}
