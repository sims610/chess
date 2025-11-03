package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {
    @Override
    public UserData read(String username, String password) throws DataAccessException {
        configureDatabase();
        var statement = "SELECT username, password, email FROM user WHERE username=?;";
        UserData databaseUser =  queryUser(statement, username);
        if (databaseUser == null) {
            return null;
        }
        if (verifyUser(databaseUser.password(), password)) {
            return databaseUser;
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    @Override
    public void create(UserData userData) throws DataAccessException {
        configureDatabase();
        var statement = "INSERT INTO `user`(`username`, `password`, `email`) VALUES (?, ?, ?);";
        String hashedPassword = hashUserPassword(userData.password());
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE user;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Couldn't Access Database");
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
            throw new DataAccessException(500, "Error: Couldn't Access Database");
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
        } catch (SQLException ex) {
            throw new DataAccessException(500, "Error: Can't configure Database");
        }
    }

    private String hashUserPassword(String password) {
        // write the hashed password in database along with the user's other information
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyUser(String hashedPassword, String providedClearTextPassword) {
        // read the previously hashed password from the database
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}
