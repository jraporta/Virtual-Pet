package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import cat.jraporta.virtualpet.core.domain.enums.Food;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@ToString
@AllArgsConstructor
@Getter
@Document("pet")
public class PetEntity {

    @Id
    private String id;

    private String name;

    private Long userId;

    private String type;

    private String color;

    private int happiness;

    private int energy;

    private int hunger;

    private String mood;

    private boolean isAsleep;

    private boolean isDead;

    private boolean hasPoo;

    private int pooUrge;

    private String location;

    private Set<Accessory> accessories;

    private Map<Accessory, Integer> accessoryPreferences;

    private Map<Location, Integer> locationPreferences;

    private Map<Food, Integer> foodPreferences;

    private Date lastInteractionDate;

    private Date updateDate;

}
