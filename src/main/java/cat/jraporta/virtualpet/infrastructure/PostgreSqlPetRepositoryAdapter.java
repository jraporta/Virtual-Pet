package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.core.domain.PetId;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class PostgreSqlPetRepositoryAdapter implements PetRepository {

    PostgreSqlPetRepository postgreSqlPetRepository;
    PetMapper petMapper;
    IdMapper idMapper;

    @Override
    public Mono<Pet> savePet(Pet pet) {
        return postgreSqlPetRepository.save(petMapper.mapToPetEntity(pet))
                .map(petMapper::mapToPet);
    }

    @Override
    public Mono<Pet> getPetById(PetId petId) {
        return postgreSqlPetRepository.findById(idMapper.mapToLong(petId))
                .map(petMapper::mapToPet);
    }
}
