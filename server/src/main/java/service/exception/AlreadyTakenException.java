package service.exception;

public class AlreadyTakenException extends ServiceException {
    public AlreadyTakenException() {
        super("already taken", 403);
    }

    public AlreadyTakenException(String message) {
        super(message, 403);
    }
}