package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.PetId;
import reactor.core.publisher.Mono;

public interface PetService {

    Mono<Pet> savePet(Pet pet);

    Mono<Pet> getPetById(PetId petId);

}
