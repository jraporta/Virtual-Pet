package cat.jraporta.virtualpet.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Pet creation request", description = "JSON with the Pet data")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PetCreationRequest {

    @Schema(description = "Pet name", example = "Doraemon")
    private String name;

    @Schema(description = "Pet type", example = "DOG")
    private String type;

    @Schema(description = "Pet color", example = "#917F5F")
    private String color;

}
