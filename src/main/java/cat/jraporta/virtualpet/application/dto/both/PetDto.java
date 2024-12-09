package cat.jraporta.virtualpet.application.dto.both;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Pet", description = "JSON with the Pet data")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PetDto {

    @Schema(description = "Pet id", example = "57")
    private Long id;

    @Schema(description = "Pet name", example = "Doraemon")
    private String name;

    @Schema(description = "Owner id", example = "102")
    private Long userId;

}
