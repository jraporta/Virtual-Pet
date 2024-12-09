package cat.jraporta.virtualpet.infrastructure.exception;

public class UnauthorizedActionException extends RuntimeException{

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
