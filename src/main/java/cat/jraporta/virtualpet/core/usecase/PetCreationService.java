package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Species;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PetCreationService<ID> {

    DomainPetService<ID> petService;

    public PetCreationService(DomainPetService<ID> petService) {
        this.petService = petService;
    }

    public Mono<Pet<ID>> createPet(String name, Species species, String color, ID userId) {
        Pet<ID> pet = (Pet<ID>) Pet.builder()
                .name(name)
                .userId(userId)
                .species(species)
                .color(color)
                .happiness(50)
                .energy(50)
                .mood(Mood.HAPPY)
                .location(Location.ROOM)
                .build();
        log.debug("create pet: {}", pet);
        return petService.savePet(pet);
    }
}
