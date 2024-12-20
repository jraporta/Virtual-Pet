package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.infrastructure.persistence.entity.PetEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PostgreSqlPetRepository extends R2dbcRepository<PetEntity, Long> {

    Flux<PetEntity> findByUserId(Long userId);

}
