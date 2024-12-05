package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.infrastructure.persistence.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface AuthenticationRepository {

    Mono<UserEntity> signUp(UserEntity user);

    Mono<UserEntity> findByUsername(String name);

    Mono<Boolean> existsByName(String name);

}
