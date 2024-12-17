package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.*;
import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Pet<ID> {

    private ID id;
    private String name;
    private ID userId;
    private Type type;
    private String color;
    private int happiness;
    private int energy;
    private Mood mood;
    private boolean isAsleep;
    private boolean hasPoo;
    private Location location;
    private Set<Accessory> accessories;
    private Map<Accessory, Integer> accessoryPreferences;
    private Map<Location, Integer> locationPreferences;
    private Map<Food, Integer> foodPreferences;
    private Date updateDate;

    public static class PetBuilder<ID> {
    }
}
