package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PostgreSqlUserRepository extends R2dbcRepository<UserEntity, Long> {

    Mono<UserEntity> findByName(String name);

}
