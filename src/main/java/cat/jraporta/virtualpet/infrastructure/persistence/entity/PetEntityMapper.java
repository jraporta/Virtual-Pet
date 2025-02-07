package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.Stat;
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
                pet.getHappiness().getValue(),
                pet.getEnergy().getValue(),
                pet.getMood().name(),
                pet.isAsleep(),
                pet.isDead(),
                pet.isHasPoo(),
                pet.getPooUrge().getValue(),
                pet.getLocation().name(),
                pet.getAccessories(),
                pet.getAccessoryPreferences(),
                pet.getLocationPreferences(),
                pet.getFoodPreferences(),
                pet.getLastInteractionDate(),
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
                new Stat(petEntity.getHappiness()),
                new Stat(petEntity.getEnergy()),
                Mood.valueOf(petEntity.getMood()),
                petEntity.isAsleep(),
                petEntity.isDead(),
                petEntity.isHasPoo(),
                new Stat(petEntity.getPooUrge()),
                Location.valueOf(petEntity.getLocation()),
                petEntity.getAccessories(),
                petEntity.getAccessoryPreferences(),
                petEntity.getLocationPreferences(),
                petEntity.getFoodPreferences(),
                petEntity.getLastInteractionDate(),
                petEntity.getUpdateDate()
        );
    }

}
