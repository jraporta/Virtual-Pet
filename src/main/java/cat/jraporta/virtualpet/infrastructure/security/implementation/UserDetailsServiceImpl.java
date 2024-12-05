package cat.jraporta.virtualpet.infrastructure.security.implementation;

import cat.jraporta.virtualpet.infrastructure.exception.InvalidCredentialsException;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final AuthenticationRepository authenticationRepository;

    public UserDetailsServiceImpl(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authenticationRepository.findByUsername(username)
                .map(userEntity -> User.builder()
                        .username(userEntity.getName())
                        .password(userEntity.getPassword())
                        .roles(userEntity.getRole().name())
                        .build())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("User not found")));
    }
}
