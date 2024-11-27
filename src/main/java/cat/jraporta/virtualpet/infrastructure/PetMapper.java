package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.core.domain.Pet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PetMapper {

    private IdMapper idMapper;

    public PetEntity mapToPetEntity(Pet pet){
        return new PetEntity(idMapper.mapToLong(pet.getPetId()), pet.getName());
    }

    public Pet mapToPet(PetEntity petEntity){
        return new Pet(idMapper.mapToPetId(petEntity.getId()), petEntity.getName());
    }

}
