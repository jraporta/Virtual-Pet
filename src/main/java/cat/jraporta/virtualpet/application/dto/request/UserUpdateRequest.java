package cat.jraporta.virtualpet.application.dto.request;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(name = "User update request", description = "JSON with the User data")
@AllArgsConstructor
@Getter
public class UserUpdateRequest {

    @Schema(description = "User id", example = "102")
    private String id;

    @Schema(description = "User name", example = "Nobita")
    private String name;

    @Schema(description = "User role", examples = {"USER", "ADMIN"})
    private Role role;

}
