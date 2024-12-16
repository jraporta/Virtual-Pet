package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PetFactory<ID> {

    public Mono<Pet<ID>> createPet(String name, Type type, String color, ID userId) {
        return Mono.just(
                Pet.<ID>builder()
                .name(name)
                .userId(userId)
                .type(type)
                .color(color)
                .happiness(50)
                .energy(50)
                .mood(Mood.HAPPY)
                .location(Location.ROOM)
                .build()
        );
    }
}
