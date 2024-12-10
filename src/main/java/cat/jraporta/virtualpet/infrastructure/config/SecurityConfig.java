package cat.jraporta.virtualpet.infrastructure.config;

import cat.jraporta.virtualpet.infrastructure.security.CustomCorsConfiguration;
import cat.jraporta.virtualpet.infrastructure.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomCorsConfiguration customCorsConfiguration;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .pathMatchers("*/swagger-ui/*", "/swagger-ui.html", "*/api-docs/*", "*/api-docs*").permitAll()
                        .anyExchange().authenticated())
                .addFilterAfter(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .cors(corsSpec -> corsSpec.configurationSource(customCorsConfiguration))
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(){
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                 new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
