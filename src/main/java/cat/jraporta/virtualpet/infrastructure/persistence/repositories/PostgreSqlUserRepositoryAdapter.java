package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.exceptions.AlreadyExistingUserException;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import cat.jraporta.virtualpet.core.exceptions.EntityNotFoundException;
import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntity;
import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntityMapper;
import cat.jraporta.virtualpet.infrastructure.security.LoginRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlUserRepositoryAdapter implements UserRepository<Long>, LoginRepository {

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
        return findByUsername(name)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<UserEntity> findByUsername(String name) {
        log.debug("Get user entity with name: {}", name);
        return postgreSqlUserRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No user found with name: " + name)));
    }

    @Override
    public Mono<UserEntity> signUp(UserEntity user) {
        return findByName(user.getName())
                .onErrorResume(e -> Mono.empty())
                .flatMap(existingUser -> {
                    log.debug("Unable to create user, there is already a user with the name: {}", user);;
                    return Mono.<UserEntity>error(new AlreadyExistingUserException(
                            "Invalid username: there is already an user with the name " + existingUser.getName()));
                })
                .switchIfEmpty(postgreSqlUserRepository.save(user))
                .doOnNext(savedUser -> log.debug("New user created: {}", savedUser));
    }
}
