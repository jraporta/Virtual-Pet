package cat.jraporta.virtualpet.infrastructure.persistence.repositories;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import cat.jraporta.virtualpet.infrastructure.exception.EntityNotFoundException;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.UserEntity;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.UserEntityMapper;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Repository
public class PostgreSqlUserRepositoryAdapter implements UserRepository<String>, AuthenticationRepository {

    private final PostgreSqlUserRepository postgreSqlUserRepository;
    private final MongoDbPetRepositoryAdapter petRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Mono<User<String>> saveUser(User<String> user) {
        log.debug("Save user: {}", user);
        return postgreSqlUserRepository.save(userEntityMapper.toEntity(user))
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<User<String>> findById(String id) {
        log.debug("Get user with id: {}", id);
        return postgreSqlUserRepository.findById(Long.valueOf(id))
                .flatMap(this::addPets)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<User<String>> findByName(String name) {
        log.debug("Get user with name: {}", name);
        return findByUsername(name)
                .flatMap(this::addPets)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Mono<List<User<String>>> findAll() {
        log.debug("Find all the users");
        return postgreSqlUserRepository.findAll()
                .map(userEntityMapper::toDomain)
                .collectList();
    }

    @Override
    public Mono<UserEntity> findByUsername(String username) {
        log.debug("Get user entity with name: {}", username);
        return postgreSqlUserRepository.findByName(username)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No user found with name: " + username)));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return postgreSqlUserRepository.existsByName(name);
    }

    @Override
    public Mono<UserEntity> signUp(UserEntity user) {
        log.debug("sign up user: {}", user.getName());
        return postgreSqlUserRepository.save(user);
    }

    private Mono<UserEntity> addPets(UserEntity user){
        return petRepository.findByUserId(user.getId())
                .collectList()
                .map(pets -> {
                    user.setPets(pets);
                    return user;
                });
    }
}
