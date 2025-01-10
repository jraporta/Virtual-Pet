package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.infrastructure.persistence.entity.PetEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MongoDbPetRepository extends ReactiveMongoRepository<PetEntity, String> {

    Flux<PetEntity> findByUserId(Long userId);

}
