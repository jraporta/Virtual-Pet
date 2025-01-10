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
public class MongoDbPetRepositoryAdapter implements PetRepository<String> {

    private final MongoDbPetRepository mongoDbPetRepository;
    private final PetEntityMapper petEntityMapper;

    @Override
    public Mono<Pet<String>> savePet(Pet<String> pet) {
        log.debug("Save pet: {}", pet.toString());
        return mongoDbPetRepository.save(petEntityMapper.toEntity(pet))
                .map(petEntityMapper::toDomain);
    }

    @Override
    public Mono<Pet<String>> findById(String id) {
        log.debug("Get pet with id: {}", id);
        return mongoDbPetRepository.findById(id)
                .map(petEntityMapper::toDomain)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No pet found with this id")));
    }

    @Override
    public Flux<Pet<String>> findAll() {
        return mongoDbPetRepository.findAll()
                .map(petEntityMapper::toDomain);
    }

    @Override
    public Mono<Void> deletePet(String id) {
        return mongoDbPetRepository.deleteById(id);
    }

    Flux<PetEntity> findByUserId(Long userId){
        return mongoDbPetRepository.findByUserId(userId);
    }
}
