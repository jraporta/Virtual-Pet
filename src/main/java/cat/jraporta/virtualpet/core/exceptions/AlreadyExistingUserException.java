package cat.jraporta.virtualpet.core.exceptions;

public class AlreadyExistingUserException extends RuntimeException{

    public AlreadyExistingUserException(String message) {
        super(message);
    }
}
