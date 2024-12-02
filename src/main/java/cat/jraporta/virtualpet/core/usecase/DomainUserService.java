package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.exceptions.AlreadyExistingUserException;
import cat.jraporta.virtualpet.core.port.in.UserService;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class DomainUserService<ID> implements UserService<ID> {

    private final UserRepository<ID> userRepository;

    @Override
    public Mono<User<ID>> saveUser(User<ID> user) {
        return userRepository.findByName(user.getName())
                .onErrorResume(e -> Mono.empty())
                .flatMap(existingUser -> {
                    log.debug("Unable to create user, there is already a user with the name: {}", user);;
                    return Mono.<User<ID>>error(new AlreadyExistingUserException(
                            "Invalid username: there is already an user with the name " + existingUser.getName()));
                })
                .switchIfEmpty(userRepository.saveUser(user))
                .doOnNext(savedUser -> log.debug("New user created: {}", savedUser));
    }

    @Override
    public Mono<User<ID>> getUserById(ID id) {
        log.debug("get user with id: {}", id);
        return userRepository.findById(id);
    }

    @Override
    public Mono<User<ID>> getUserByUsername(String name) {
        log.debug("get user with name: {}", name);
        return userRepository.findByName(name);
    }

}
