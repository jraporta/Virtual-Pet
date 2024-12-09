package cat.jraporta.virtualpet.application.dto;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class UserDto {

    private Long id;
    private String name;
    private Role role;
    private List<PetDto> pets = new ArrayList<>();

}
