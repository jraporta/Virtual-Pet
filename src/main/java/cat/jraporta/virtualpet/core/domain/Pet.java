package cat.jraporta.virtualpet.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pet {

    private PetId petId;

    private String name;

}
