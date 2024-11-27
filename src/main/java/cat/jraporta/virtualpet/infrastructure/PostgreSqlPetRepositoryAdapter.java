package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import cat.jraporta.virtualpet.infrastructure.entity.PetEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlPetRepositoryAdapter implements PetRepository<Long> {

    PostgreSqlPetRepository postgreSqlPetRepository;
    PetEntityMapper petEntityMapper;

    @Override
    public Mono<Pet<Long>> savePet(Pet<Long> pet) {
        log.debug("Save pet: {}", pet.toString());
        return postgreSqlPetRepository.save(petEntityMapper.toEntity(pet))
                .map(petEntityMapper::toDomain);
    }

    @Override
    public Mono<Pet<Long>> getPetById(Long id) {
        log.debug("Get pet with id: {}", id);
        return postgreSqlPetRepository.findById(id)
                .map(petEntityMapper::toDomain);
    }
}
