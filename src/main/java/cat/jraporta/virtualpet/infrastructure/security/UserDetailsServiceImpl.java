package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import reactor.core.publisher.Mono;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final PostgreSqlUserRepository postgreSqlUserRepository;

    public UserDetailsServiceImpl(PostgreSqlUserRepository postgreSqlUserRepository) {
        this.postgreSqlUserRepository = postgreSqlUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return postgreSqlUserRepository
                .findByName(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(username + " not found")))
                .block();
    }

}
