package service.exception;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException() {
        super("unauthorized", 401);
    }

    public UnauthorizedException(String message) {
        super(message, 401);
    }
}