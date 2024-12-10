package cat.jraporta.virtualpet.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "Register credentials", description = "JSON with the credentials of the User")
@Getter
@AllArgsConstructor
public class SignUpRequest {

    @Schema(description = "Username", example = "Nobita")
    @NotBlank(message = "User is a required field")
    String user;

    @Schema(description = "User password", example = "myPassword")
    @NotBlank(message = "Password is a required field")
    String password;

}
