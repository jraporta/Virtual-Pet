package cat.jraporta.virtualpet.application.dto.response;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Schema(name = "User", description = "JSON with the User data")
@AllArgsConstructor
@Getter
public class UserDto {

    @Schema(description = "User id", example = "102")
    private String id;

    @Schema(description = "User name", example = "Nobita")
    private String name;

    @Schema(description = "User role", examples = {"USER", "ADMIN"})
    private Role role;

    @ArraySchema(schema = @Schema(implementation = PetDto.class))
    private List<PetDto> pets = new ArrayList<>();

}
