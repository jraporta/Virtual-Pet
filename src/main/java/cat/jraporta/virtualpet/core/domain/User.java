package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class User<ID> {

    private ID id;
    private String name;
    private String password;
    private Role role;

}
