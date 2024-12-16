package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Species;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PetFactory<ID> {

    public Mono<Pet<ID>> createPet(String name, Species species, String color, ID userId) {
        return Mono.just(
                Pet.<ID>builder()
                .name(name)
                .userId(userId)
                .species(species)
                .color(color)
                .happiness(50)
                .energy(50)
                .mood(Mood.HAPPY)
                .location(Location.ROOM)
                .build()
        );
    }
}
