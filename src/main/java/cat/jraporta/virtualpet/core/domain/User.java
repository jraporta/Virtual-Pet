package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class User<ID> {

    private ID id;
    private String name;
    private String password;
    private Role role;
    private List<Pet<ID>> pets = new ArrayList<>();

}
