package cat.jraporta.virtualpet.application.mapper;

import cat.jraporta.virtualpet.application.dto.both.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetDtoMapper {

    public PetDto toDto(Pet<Long> pet){
        return new PetDto(pet.getId(), pet.getName(), pet.getUserId());
    }

    public Pet<Long> toDomain(PetDto petDto){
        return new Pet<Long>(petDto.getId(), petDto.getName(), petDto.getUserId());
    }

}
