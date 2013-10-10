package db;

public class NotUniqueLoginException extends DBException {
    public NotUniqueLoginException(String message) {
        super(message);
    }

    public NotUniqueLoginException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
