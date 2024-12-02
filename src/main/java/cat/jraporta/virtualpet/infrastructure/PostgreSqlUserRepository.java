package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.infrastructure.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PostgreSqlUserRepository extends R2dbcRepository<UserEntity, Long> {

    @Query("SELECT * FROM user WHERE 'name' = ?1")
    Mono<UserEntity> findByName(String name);

}
