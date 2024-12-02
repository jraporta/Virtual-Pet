package cat.jraporta.virtualpet.infrastructure.config;

import cat.jraporta.virtualpet.infrastructure.persistence.repositories.PostgreSqlUserRepository;
import cat.jraporta.virtualpet.infrastructure.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
        return http. build();
    }

    @Bean
    public UserDetailsService userDetailsService(PostgreSqlUserRepository postgreSqlUserRepository){
        return new UserDetailsServiceImpl(postgreSqlUserRepository);
    }


}
