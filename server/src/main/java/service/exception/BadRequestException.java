package service.exception;

public class BadRequestException extends ServiceException {
    public BadRequestException() {
        super("bad request", 400);
    }

    public BadRequestException(String message) {
        super(message, 400);
    }
}
