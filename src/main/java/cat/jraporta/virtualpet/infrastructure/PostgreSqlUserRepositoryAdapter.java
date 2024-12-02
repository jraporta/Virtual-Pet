package cat.jraporta.virtualpet.infrastructure;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import cat.jraporta.virtualpet.infrastructure.entity.UserEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlUserRepositoryAdapter implements UserRepository<Long> {

    PostgreSqlUserRepository postgreSqlUserRepository;
    UserEntityMapper userEntityMapper;

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
                .map(userEntityMapper::toDomain);
    }
}
