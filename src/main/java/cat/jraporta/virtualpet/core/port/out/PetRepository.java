package cat.jraporta.virtualpet.core.port.out;

import cat.jraporta.virtualpet.core.domain.PetId;
import cat.jraporta.virtualpet.core.domain.Pet;
import reactor.core.publisher.Mono;

public interface PetRepository {

    Mono<Pet> savePet(Pet pet);
    Mono<Pet> getPetById(PetId petId);

}
