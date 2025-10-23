package dataaccess;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    Integer code;

    public DataAccessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public Integer httpStatus() {
        return code;
    }
}
