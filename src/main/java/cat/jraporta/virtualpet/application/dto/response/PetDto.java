package cat.jraporta.virtualpet.application.dto.response;

import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class PetDto {

    @Schema(description = "Pet id", example = "57")
    private String id;

    @Schema(description = "Pet name", example = "Doraemon")
    private String name;

    @Schema(description = "Owner id", example = "102")
    private String userId;

    @Schema(description = "species", example = "dog")
    private String species;

    @Schema(description = "color", example = "#917F5F")
    private String color;

    @Schema(description = "happiness", example = "50")
    private int happiness;

    @Schema(description = "energy", example = "75")
    private int energy;

    @Schema(description = "mood", example = "happy")
    private String mood;

    @Schema(description = "is asleep", example = "false")
    private boolean isAsleep;

    @Schema(description = "is dead", example = "false")
    private boolean isDead;

    @Schema(description = "has poo", example = "true")
    private boolean hasPoo;

    @Schema(description = "location", example = "Seaside")
    private String location;

    @Schema(description = "accessories", example = "[\"hat\", \"sunglasses\"]")
    private Set<Accessory> accessories;

}
