package cat.jraporta.virtualpet.infrastructure;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgreSqlPetRepository extends R2dbcRepository<PetEntity, Long> {
}
