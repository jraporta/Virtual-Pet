package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import cat.jraporta.virtualpet.core.exceptions.EntityNotFoundException;
import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlUserRepositoryAdapter implements UserRepository<Long> {

    private final PostgreSqlUserRepository postgreSqlUserRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Mono<User<Long>> saveUser(User<Long> user) {
        log.debug("Save user: {}", user);
        return postgreSqlUserRepository.save(userEntityMapper.toEntity(user))
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<User<Long>> findById(Long id) {
        log.debug("Get user with id: {}", id);
        return postgreSqlUserRepository.findById(id)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<User<Long>> findByName(String name) {
        log.debug("Get user with name: {}", name);
        return postgreSqlUserRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No user found with name: " + name)))
                .map(userEntityMapper::toDomain);
    }
}
