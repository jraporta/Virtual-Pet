package cat.jraporta.virtualpet.application.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Schema(name = "Valid pet types", description = "JSON with available pet types.")
@Getter
@AllArgsConstructor
public class ValidPetTypesRequest {

    @ArraySchema(schema = @Schema(type = "string",
            description = "Pet type",
            example = "[\"DOG\", \"CAT\", \"COW\"]"))
    private List<String> types;

}
