package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.User;
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

    @Override
    public Mono<Pet<ID>> savePet(Pet<ID> pet) {
        log.debug("save pet: {}", pet);
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> getPetById(ID id) {
        log.debug("get pet with id: {}", id);
        return petRepository.findById(id);
    }

    @Override
    public Mono<List<Pet<ID>>> getAllPetsOfUser(String name) {
        return domainUserService.getUserByUsername(name)
                .map(User::getPets);
    }

    @Override
    public Flux<Pet<ID>> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public Mono<Pet<ID>> updatePet(Pet<ID> pet) {
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Void> checkOwnershipOfPet(String username, ID petId) {
        return domainUserService.getUserByUsername(username)
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
