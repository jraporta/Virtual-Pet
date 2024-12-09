package cat.jraporta.virtualpet.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class PetDto {

    private Long id;
    private String name;
    private Long userId;

}
