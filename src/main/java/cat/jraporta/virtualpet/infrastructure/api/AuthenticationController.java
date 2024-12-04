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

    @PostMapping("api/users")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signUp(@RequestBody SignUpRequest request){
        log.debug("Sign up request: {}", request.getUser());
        return authenticationService.signUp(request)
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(token));
    }

    @GetMapping("api/users")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signIn(@RequestBody SignInRequest request){
        log.debug("Sign in request: {}", request.getUser());
        return authenticationService.signIn(request)
                .map(ResponseEntity::ok);
    }


}
