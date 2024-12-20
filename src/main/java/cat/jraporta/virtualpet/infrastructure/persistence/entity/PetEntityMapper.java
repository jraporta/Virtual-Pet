package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;

public class PetEntityMapper {

    public PetEntity toEntity(Pet<Long> pet){
        return new PetEntity(pet.getId(), pet.getName(), pet.getUserId());
    }

    public Pet<Long> toDomain(PetEntity petEntity){
        return new Pet<>(petEntity.getId(), petEntity.getName(), petEntity.getUserId());
    }

}
