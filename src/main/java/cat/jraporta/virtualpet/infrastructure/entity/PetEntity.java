package cat.jraporta.virtualpet.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@AllArgsConstructor
@Getter
@Table("pet")
public class PetEntity {

    @Id
    private Long id;

    private String name;

}
