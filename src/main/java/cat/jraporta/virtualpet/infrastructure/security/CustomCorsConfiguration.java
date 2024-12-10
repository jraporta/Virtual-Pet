package cat.jraporta.virtualpet.infrastructure.security;


import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class CustomCorsConfiguration implements CorsConfigurationSource {

    @Override
    public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        return config;
    }
}
