package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.Pet;
import reactor.core.publisher.Mono;

public interface PetService<ID> {

    Mono<Pet<ID>> savePet(Pet<ID> pet);
    Mono<Pet<ID>> getPetById(ID id);

}
