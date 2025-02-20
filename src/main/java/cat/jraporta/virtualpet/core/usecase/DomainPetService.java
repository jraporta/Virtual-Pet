package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.PetFactory;
import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import cat.jraporta.virtualpet.core.port.in.PetService;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import cat.jraporta.virtualpet.infrastructure.exception.UnauthorizedActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DomainPetService<ID> implements PetService<ID> {

    private final PetRepository<ID> petRepository;
    private final DomainUserService<ID> domainUserService;
    private final PetFactory<ID> petFactory;


    @Override
    public Mono<Pet<ID>> createPet(String name, Type type, String color, ID userId) {
        return petFactory.createPet(name, type, color, userId)
                .flatMap(pet -> {
                    log.debug("create pet: {}", pet);
                    return savePet(pet);
                });
    }

    @Override
    public Mono<Pet<ID>> savePet(Pet<ID> pet) {
        log.debug("save pet: {}", pet);
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> getPetById(ID id) {
        log.debug("get pet with id: {}", id);
        return petRepository.findById(id)
                .flatMap(pet -> {
                    pet.refresh();
                    return petRepository.savePet(pet);
                });
    }

    @Override
    public Mono<List<Pet<ID>>> getAllPetsOfUser(String name) {
        return domainUserService.getUserByName(name)
                .map(User::getPets)
                .flatMapMany(Flux::fromIterable)
                .flatMap(pet -> {
                    pet.refresh();
                    return petRepository.savePet(pet);
                })
                .collectList();
    }

    @Override
    public Flux<Pet<ID>> getAllPets() {
        return petRepository.findAll()
                .flatMap(pet -> {
                    pet.refresh();
                    return petRepository.savePet(pet);
                });
    }

    @Override
    public Mono<Void> checkOwnershipOfPet(String username, ID petId) {
        return domainUserService.getUserByName(username)
                .flatMap(user -> getPetById(petId)
                        .flatMap(pet -> {
                            if (pet.getUserId().equals(user.getId())) return Mono.empty();
                            return Mono.error(new UnauthorizedActionException(
                                    "Unauthorized action: Only the pet owner is allowed to perform this operation."));
                        })
                );
    }

    @Override
    public Mono<Void> deletePet(ID id) {
        return petRepository.deletePet(id);
    }


}
