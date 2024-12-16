package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@Builder
public class Pet<ID> {

    private ID id;
    private String name;
    private ID userId;
    private Species species;
    private String color;
    private int happiness;
    private int energy;
    private Mood mood;
    private Location location;
    /*
    private Set<Accessory> accessories;
    private Map<Accessory, Integer> accessoryPreferences;
    private Map<Location, Integer> locationPreferences;
    private Map<Food, Integer> foodPreferences;
*/
    public static class PetBuilder<ID> {
    }
}
