package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.core.domain.PetId;
import org.springframework.stereotype.Component;

@Component
public class IdMapper {

    public Long mapToLong(PetId petId){
        return 1L;
    }

    public PetId mapToPetId(Long id) {
        return null;
    }
}
