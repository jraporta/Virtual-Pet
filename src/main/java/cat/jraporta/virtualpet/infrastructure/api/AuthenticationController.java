package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.dto.request.SignInRequest;
import cat.jraporta.virtualpet.application.dto.request.SignUpRequest;
import cat.jraporta.virtualpet.application.dto.response.JwtAuthenticationResponse;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("api/register")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signUp(@RequestBody SignUpRequest request){
        log.debug("Sign up request: {}", request.getUser());
        return authenticationService.signUp(request)
                .doOnNext(unused -> log.debug("Register successful for {}, sent token", request.getUser()))
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(token))
                .doOnError(error -> log.warn("Registration failed for user: {}, error: {}",
                        request.getUser(), error.getMessage()));
    }

    @GetMapping("api/login")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signIn(@RequestBody SignInRequest request){
        log.debug("Sign in request: {}", request.getUser());
        return authenticationService.signIn(request)
                .doOnNext(unused -> log.debug("Login successful for {}, sent token", request.getUser()))
                .map(ResponseEntity::ok)
                .doOnError(error -> log.warn("Login failed for user: {}, error: {}",
                        request.getUser(), error.getMessage()));
    }


}
