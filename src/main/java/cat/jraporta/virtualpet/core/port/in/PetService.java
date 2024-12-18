package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PetService<ID> {

    Mono<Pet<ID>> createPet(String name, Type type, String color, ID userId);
    Mono<Pet<ID>> savePet(Pet<ID> pet);
    Mono<Pet<ID>> getPetById(ID id);
    Mono<List<Pet<ID>>> getAllPetsOfUser(String name);
    Flux<Pet<ID>> getAllPets();
    Mono<Pet<ID>> updatePet(Pet<ID> pet);
    Mono<Void> checkOwnershipOfPet(String username, ID petId);
    Mono<Void> deletePet(ID id);

}
