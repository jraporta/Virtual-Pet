package cat.jraporta.virtualpet.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "JWT token", description = "JSON with the authentication token")
@Getter
@AllArgsConstructor
public class JwtAuthenticationResponse {

    @Schema(description = "JWT token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZWdldHRhIiwiaWF0IjoxNzMzNzQ0MzM2LCJleHAiOjE3MzM3NDU1MzZ9.23Hz5V_y_2kmlpLXz-a0RkpBCY6kR7Q2p321xZVlQkQ")
    private String token;

}
