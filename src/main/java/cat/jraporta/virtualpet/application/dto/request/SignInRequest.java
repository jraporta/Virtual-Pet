package cat.jraporta.virtualpet.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "Login credentials", description = "JSON with the credentials of the User")
@Getter
@AllArgsConstructor
public class SignInRequest {

    @Schema(description = "Username", example = "Nobita")
    @NotNull(message = "User is a required field")
    String user;

    @Schema(description = "User password", example = "myPassword")
    @NotNull(message = "Password is a required field")
    String password;

}
