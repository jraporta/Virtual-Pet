package cat.jraporta.virtualpet.application.mapper;

import cat.jraporta.virtualpet.application.dto.response.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
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
                pet.isAsleep(),
                pet.getPooCountdown() == 100,
                pet.getLocation().name(),
                pet.getAccessories()
        );
    }

}
