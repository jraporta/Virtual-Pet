package cat.jraporta.virtualpet.infrastructure.security.implementation;

import cat.jraporta.virtualpet.application.dto.request.SignInRequest;
import cat.jraporta.virtualpet.application.dto.request.SignUpRequest;
import cat.jraporta.virtualpet.application.dto.response.JwtAuthenticationResponse;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.infrastructure.exception.AlreadyExistingUserException;
import cat.jraporta.virtualpet.infrastructure.exception.InvalidCredentialsException;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.UserEntity;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationRepository;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationService;
import cat.jraporta.virtualpet.infrastructure.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ReactiveAuthenticationManager authenticationManager;
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(ReactiveAuthenticationManager authenticationManager,
                                     AuthenticationRepository authenticationRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<JwtAuthenticationResponse> signIn(SignInRequest request) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUser(), request.getPassword()))
                .map(auth -> new JwtAuthenticationResponse(jwtService.generateToken((UserDetails) auth.getPrincipal())))
                .onErrorResume(BadCredentialsException.class, e ->
                        Mono.error(new InvalidCredentialsException("Invalid Credentials")));
    }

    @Override
    public Mono<JwtAuthenticationResponse> signUp(SignUpRequest request) {
        UserEntity user = UserEntity.builder()
                .name(request.getUser())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return saveUserIfNotExists(user)
                .flatMap(unused -> Mono.just(new JwtAuthenticationResponse(
                        jwtService.generateToken(User.withUsername(user.getName()).password("").build()))));
    }

    private Mono<UserEntity> saveUserIfNotExists(UserEntity user) {
        return authenticationRepository.existsByName(user.getName())
                .flatMap(exists -> {
                    if (exists) {
                        log.debug("Unable to create user, there is already a user with the name: {}", user);
                        return Mono.<UserEntity>error(new AlreadyExistingUserException(
                                "Invalid username: there is already an user with the name " + user.getName()));
                    }
                    return authenticationRepository.signUp(user);
                })
                .doOnNext(savedUser -> log.debug("New user created: {}", savedUser));
    }
}


