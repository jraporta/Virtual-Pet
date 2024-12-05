package cat.jraporta.virtualpet.infrastructure.exception;

public class InvalidJwtException extends RuntimeException{

    public InvalidJwtException(String message) {
        super(message);
    }
}
