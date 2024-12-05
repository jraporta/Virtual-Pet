package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@ToString
@AllArgsConstructor
@Getter
@Table("player")
public class UserEntity {

    @Id
    private Long id;

    private String name;

    private String password;

    private Role role;

}
