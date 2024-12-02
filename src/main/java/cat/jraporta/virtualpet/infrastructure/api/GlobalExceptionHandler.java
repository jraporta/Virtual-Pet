package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.core.exceptions.AlreadyExistingUserException;
import cat.jraporta.virtualpet.core.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<String>> handleEntityNotFound(EntityNotFoundException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistingUserException.class)
    public Mono<ResponseEntity<String>> handleAlreadyExistingUser(AlreadyExistingUserException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

}
