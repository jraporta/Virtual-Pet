package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final PostgreSqlUserRepository postgreSqlUserRepository;

    public UserDetailsServiceImpl(PostgreSqlUserRepository postgreSqlUserRepository) {
        this.postgreSqlUserRepository = postgreSqlUserRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return postgreSqlUserRepository
                .findByName(username)
                .map(userEntity -> (UserDetails) userEntity)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(username + " not found")));
    }
}
