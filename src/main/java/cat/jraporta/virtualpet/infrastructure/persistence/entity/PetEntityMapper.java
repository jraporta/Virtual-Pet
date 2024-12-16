package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Species;

public class PetEntityMapper {

    public PetEntity toEntity(Pet<String> pet){
        return new PetEntity(
                pet.getId(),
                pet.getName(),
                Long.valueOf(pet.getUserId()),
                pet.getSpecies().name(),
                pet.getColor(),
                pet.getHappiness(),
                pet.getEnergy(),
                pet.getMood().name(),
                pet.getLocation().name()
        );
    }

    public Pet<String> toDomain(PetEntity petEntity){
        return new Pet<>(
                petEntity.getId(),
                petEntity.getName(),
                petEntity.getUserId().toString(),
                Species.valueOf(petEntity.getSpecies()),
                petEntity.getColor(),
                petEntity.getHappiness(),
                petEntity.getEnergy(),
                Mood.valueOf(petEntity.getMood()),
                Location.valueOf(petEntity.getLocation())
        );
    }

}
