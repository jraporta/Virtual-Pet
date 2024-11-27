package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.PetId;
import cat.jraporta.virtualpet.core.port.in.PetService;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class DomainPetService implements PetService {

    PetRepository petRepository;

    @Override
    public Mono<Pet> savePet(Pet pet) {
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet> getPetById(PetId petId) {
        return petRepository.getPetById(petId);
    }
}
