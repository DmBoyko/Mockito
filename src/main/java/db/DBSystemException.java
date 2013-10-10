package db;


public class DBSystemException extends DBException {
    public DBSystemException(String message) {
        super(message);
    }

    public DBSystemException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
