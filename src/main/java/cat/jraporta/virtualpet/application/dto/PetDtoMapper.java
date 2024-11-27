package cat.jraporta.virtualpet.application.dto;

import cat.jraporta.virtualpet.core.domain.Pet;


public class PetDtoMapper {

    public PetDto toDto(Pet<Long> pet){
        return new PetDto(pet.getId(), pet.getName());
    }

    public Pet<Long> toDomain(PetDto petDto){
        return new Pet<Long>(petDto.getId(), petDto.getName());
    }

}
