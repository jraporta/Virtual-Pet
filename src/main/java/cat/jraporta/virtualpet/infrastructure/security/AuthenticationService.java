package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.application.dto.request.SignInRequest;
import cat.jraporta.virtualpet.application.dto.request.SignUpRequest;
import cat.jraporta.virtualpet.application.dto.response.JwtAuthenticationResponse;
import reactor.core.publisher.Mono;

public interface AuthenticationService {

    Mono<JwtAuthenticationResponse> signIn(SignInRequest request);

    Mono<JwtAuthenticationResponse> signUp(SignUpRequest request);

}
