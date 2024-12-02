package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.port.in.PetService;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class DomainPetService<ID> implements PetService<ID> {

    PetRepository<ID> petRepository;

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
}
