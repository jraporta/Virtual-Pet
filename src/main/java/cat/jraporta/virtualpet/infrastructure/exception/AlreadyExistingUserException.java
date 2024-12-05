package cat.jraporta.virtualpet.infrastructure.exception;

public class AlreadyExistingUserException extends RuntimeException{

    public AlreadyExistingUserException(String message) {
        super(message);
    }
}
