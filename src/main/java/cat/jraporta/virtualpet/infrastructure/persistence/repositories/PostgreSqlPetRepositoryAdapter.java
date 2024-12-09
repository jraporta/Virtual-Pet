package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import cat.jraporta.virtualpet.infrastructure.exception.EntityNotFoundException;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.PetEntity;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.PetEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlPetRepositoryAdapter implements PetRepository<Long> {

    private final PostgreSqlPetRepository postgreSqlPetRepository;
    private final PetEntityMapper petEntityMapper;

    @Override
    public Mono<Pet<Long>> savePet(Pet<Long> pet) {
        log.debug("Save pet: {}", pet.toString());
        return postgreSqlPetRepository.save(petEntityMapper.toEntity(pet))
                .map(petEntityMapper::toDomain);
    }

    @Override
    public Mono<Pet<Long>> findById(Long id) {
        log.debug("Get pet with id: {}", id);
        return postgreSqlPetRepository.findById(id)
                .map(petEntityMapper::toDomain)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No pet found with this id")));
    }

    @Override
    public Flux<Pet<Long>> findAll() {
        return postgreSqlPetRepository.findAll()
                .map(petEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deletePet(Long id) {
        return postgreSqlPetRepository.deleteById(id);
    }

    Flux<PetEntity> findByUserId(Long userId){
        return postgreSqlPetRepository.findByUserId(userId);
    }
}
