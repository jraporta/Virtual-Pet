package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.infrastructure.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<String>> handleInvalidCredentials(InvalidCredentialsException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidJwtException.class)
    public Mono<ResponseEntity<String>> handleInvalidJwt(InvalidJwtException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public Mono<ResponseEntity<String>> handleUnauthorizedAction(UnauthorizedActionException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<String>> HandleParameterValidationFail(WebExchangeBindException ex){
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getAllErrors().getFirst().getDefaultMessage()));
    }


}
