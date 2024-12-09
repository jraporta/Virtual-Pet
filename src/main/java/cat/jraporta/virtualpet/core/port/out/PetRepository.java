package cat.jraporta.virtualpet.core.port.out;

import cat.jraporta.virtualpet.core.domain.Pet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetRepository<ID> {

    Mono<Pet<ID>> savePet(Pet<ID> pet);
    Mono<Pet<ID>> findById(ID id);
    Flux<Pet<ID>> findAll();
    Mono<Void> deletePet(ID id);
}
