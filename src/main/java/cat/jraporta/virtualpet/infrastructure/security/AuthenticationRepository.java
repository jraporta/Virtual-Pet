package cat.jraporta.virtualpet.infrastructure.security;

import cat.jraporta.virtualpet.infrastructure.persistence.entities.UserEntity;
import reactor.core.publisher.Mono;

public interface AuthenticationRepository {

    Mono<UserEntity> signUp(UserEntity user);

    Mono<UserEntity> findByUsername(String name);

}
