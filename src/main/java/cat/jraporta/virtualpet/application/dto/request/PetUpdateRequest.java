package cat.jraporta.virtualpet.application.dto.request;

import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Schema(name = "Pet", description = "JSON with the Pet data")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PetUpdateRequest {

    @NotNull
    @Schema(description = "Pet id", example = "57")
    private String id;

    @Schema(description = "Pet name", example = "Doraemon")
    private String name;

    @Schema(description = "color", example = "#917F5F")
    private String color;

    @Schema(description = "has poo", example = "false")
    private Boolean hasPoo;

    @Schema(description = "location", example = "Seaside")
    private String location;

    @Schema(description = "accessories", example = "[\"hat\", \"sunglasses\"]")
    private Set<Accessory> accessories;

}
