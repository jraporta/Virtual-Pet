package cat.jraporta.virtualpet.application.mapper;

import cat.jraporta.virtualpet.application.dto.both.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import org.springframework.stereotype.Component;

@Component
public class PetDtoMapper {

    public PetDto toDto(Pet<String> pet){
        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getUserId(),
                pet.getType().name(),
                pet.getColor(),
                pet.getHappiness(),
                pet.getEnergy(),
                pet.getMood().name(),
                pet.getLocation().name()
        );
    }

    public Pet<String> toDomain(PetDto petDto){
        return new Pet<String>(
                petDto.getId(),
                petDto.getName(),
                petDto.getUserId(),
                Type.valueOf(petDto.getSpecies()),
                petDto.getColor(),
                petDto.getHappiness(),
                petDto.getEnergy(),
                Mood.valueOf(petDto.getMood()),
                Location.valueOf(petDto.getLocation())
        );
    }

}
