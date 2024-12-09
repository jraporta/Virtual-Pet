package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("player")
public class UserEntity {

    @Id
    private Long id;

    private String name;

    private String password;

    private Role role;

    @Transient
    private List<PetEntity> pets = new ArrayList<>();

}
