package cat.jraporta.virtualpet.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("pet")
public class PetEntity {

    @Id
    private Long id;

    private String name;

}
