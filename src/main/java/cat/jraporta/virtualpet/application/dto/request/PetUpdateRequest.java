package cat.jraporta.virtualpet.application.dto.request;

import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import cat.jraporta.virtualpet.application.dto.enums.Action;
import cat.jraporta.virtualpet.core.domain.enums.Food;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(name = "Pet update request", description = "JSON with the Pet data")
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PetUpdateRequest {

    @NotNull
    @Schema(description = "Pet id", example = "57")
    private String id;

    @Schema(description = "Action performed", example = "PLAY")
    private Action action;

    @Schema(description = "new name", example = "Doraemon")
    private String name;

    @Schema(description = "assign color", example = "#917F5F")
    private String color;

    @Schema(description = "assigned location", example = "SEASIDE")
    private Location location;

    @Schema(description = "accessory to equip/unequip from the pet", example = "HAT")
    private Accessory accessory;

    @Schema(description = "food given to the pet", example = "COOKIE")
    private Food food;
}
