package cat.jraporta.virtualpet.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean isTokenExpired(String token);

}
