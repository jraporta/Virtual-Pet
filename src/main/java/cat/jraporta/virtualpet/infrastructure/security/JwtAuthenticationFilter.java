package cat.jraporta.virtualpet.infrastructure.security;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter{

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        final String jwt;
        final String username;
        if(authHeader == null || authHeader.isEmpty() || authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }
        jwt = authHeader.substring(7);
        username = jwtService.extractUserName(jwt);
        if (!username.isEmpty()){
            /*
            return ReactiveSecurityContextHolder.getContext()
                    .switchIfEmpty(
                            userDetailsService.findByUsername(username)
                                    .flatMap(userDetails -> {
                                        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                                                userDetails, null, userDetails.getAuthorities());

                                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(exchange.getRequest()));
                                        ReactiveSecurityContextHolder.withAuthentication(authenticationToken);


                                    });

                    )
            )

             */
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }
}
