package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Type;

public class PetEntityMapper {

    public PetEntity toEntity(Pet<String> pet){
        return new PetEntity(
                pet.getId(),
                pet.getName(),
                Long.valueOf(pet.getUserId()),
                pet.getType().name(),
                pet.getColor(),
                pet.getHappiness(),
                pet.getEnergy(),
                pet.getMood().name(),
                pet.isAsleep(),
                pet.getPooCountdown(),
                pet.getLocation().name(),
                pet.getAccessories(),
                pet.getAccessoryPreferences(),
                pet.getLocationPreferences(),
                pet.getFoodPreferences(),
                pet.getUpdateDate()
        );
    }

    public Pet<String> toDomain(PetEntity petEntity){
        return new Pet<>(
                petEntity.getId(),
                petEntity.getName(),
                petEntity.getUserId().toString(),
                Type.valueOf(petEntity.getType()),
                petEntity.getColor(),
                petEntity.getHappiness(),
                petEntity.getEnergy(),
                Mood.valueOf(petEntity.getMood()),
                petEntity.isAsleep(),
                petEntity.getPooCountdown(),
                Location.valueOf(petEntity.getLocation()),
                petEntity.getAccessories(),
                petEntity.getAccessoryPreferences(),
                petEntity.getLocationPreferences(),
                petEntity.getFoodPreferences(),
                petEntity.getUpdateDate()
        );
    }

}
