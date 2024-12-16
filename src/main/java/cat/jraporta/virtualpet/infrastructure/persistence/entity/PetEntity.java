package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@AllArgsConstructor
@Getter
@Document("pet")
public class PetEntity {

    @Id
    private String id;

    private String name;

    private Long userId;

    private String species;

    private String color;

    private int happiness;

    private int energy;

    private String mood;

    private String location;

}
