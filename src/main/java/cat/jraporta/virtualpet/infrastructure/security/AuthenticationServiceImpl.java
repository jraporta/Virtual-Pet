package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.application.dto.request.SignInRequest;
import cat.jraporta.virtualpet.application.dto.request.SignUpRequest;
import cat.jraporta.virtualpet.application.dto.response.JwtAuthenticationResponse;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(LoginRepository loginRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<JwtAuthenticationResponse> signIn(SignInRequest request) {
        return loginRepository.findByUsername(request.getUser())
                .flatMap(user -> {
                    if (user.getPassword().equals(passwordEncoder.encode(request.getPassword()))) {
                        return Mono.error(new BadCredentialsException("No user with such credentials"));
                    }
                    return Mono.just(new JwtAuthenticationResponse(jwtService.generateToken(user)));
                });
    }

    @Override
    public Mono<JwtAuthenticationResponse> signUp(SignUpRequest request) {
        UserEntity user = UserEntity.builder()
                .name(request.getUser())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return loginRepository.signUp(user)
                .flatMap(unused -> Mono.just(new JwtAuthenticationResponse(jwtService.generateToken(user))));
    }
}
