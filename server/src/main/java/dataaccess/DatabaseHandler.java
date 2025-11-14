package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static dataaccess.DatabaseManager.createDatabase;

public class DatabaseHandler extends Handler {

    public DatabaseHandler() {
        configureDatabase();
    }
    @Override
    public void publish(LogRecord record) {
        try {
            try (Connection conn = DatabaseManager.getConnection()) {
                var stm = "INSERT INTO log(level, message, date) VALUES(?, ?, now())";
                try (var preparedStatement = conn.prepareStatement(stm)) {
                    preparedStatement.setString(1, record.getLevel().toString());
                    preparedStatement.setString(2, record.getMessage());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.printf("Failed to log: %s%n", ex.getMessage());
        }
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}

    void configureDatabase() {
        try (Connection conn = DatabaseManager.getConnection()) {

            final String[] createLogTable = {
                    """
                    CREATE TABLE  IF NOT EXISTS log(
                        id INT NOT NULL AUTO_INCREMENT,
                        message VARCHAR(4096) NOT NULL,
                        level VARCHAR(16) NOT NULL,
                        date DATETIME NOT NULL,
                        PRIMARY KEY(id),
                        INDEX(date)
                    )"""
            };
            for (String statement : createLogTable) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            System.out.println("There was an error logging");
        }
    }
}
