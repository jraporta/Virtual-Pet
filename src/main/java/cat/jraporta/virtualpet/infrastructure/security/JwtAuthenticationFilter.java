package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.infrastructure.exception.InvalidCredentialsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter implements WebFilter{

    private final JwtService jwtService;
    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, ReactiveUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (isPublicEndpoint(exchange)) {
            return chain.filter(exchange);  // No need to process JWT
        }
        return processJwt(exchange, chain);
    }

    private boolean isPublicEndpoint(ServerWebExchange exchange) {
        String path = exchange.getRequest().getPath().toString();
        return path.equals("/api/register") || path.equals("/api/login") || path.contains("swagger") ||
                path.contains("api-docs") || path.contains("metadata");
    }

    private Mono<Void> processJwt(ServerWebExchange exchange, WebFilterChain chain) {

        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendErrorResponse(exchange, "Missing JWT");
        }

        final String jwtToken = authHeader.substring(7);

        final String username;

        try {
            username = jwtService.extractUserName(jwtToken);
        }catch (ExpiredJwtException ex) {
            return sendErrorResponse(exchange, "Invalid JWT: token is expired");
        }catch (JwtException ex) {
            return sendErrorResponse(exchange, "Invalid JWT: token is corrupt");
        }

        if (username == null || username.isEmpty()) {
            return sendErrorResponse(exchange, "Invalid JWT: Missing username");
        }
        return userDetailsService.findByUsername(username)
                .flatMap(userDetails -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(), null, userDetails.getAuthorities());
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .onErrorResume(InvalidCredentialsException.class, e -> sendErrorResponse(exchange, "Invalid JWT: User not found"));
    }


    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);

        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

}
