package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.core.exceptions.InvalidValueException;
import cat.jraporta.virtualpet.core.exceptions.UserNotFoundException;
import cat.jraporta.virtualpet.core.port.in.UserService;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DomainUserService<ID> implements UserService<ID> {

    private final UserRepository<ID> userRepository;

    @Override
    public Mono<User<ID>> saveUser(User<ID> user) {
        return userRepository.saveUser(user)
                .doOnNext(savedUser -> log.trace("User saved: {}", savedUser));
    }

    @Override
    public Mono<User<ID>> getUserById(ID id) {
        log.debug("get user with id: {}", id);
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException("No user found with id: " + id)));
    }

    @Override
    public Mono<User<ID>> getUserByName(String name) {
        log.debug("get user with name: {}", name);
        return userRepository.findByName(name)
                .switchIfEmpty(Mono.error(new UserNotFoundException("No user found with name: " + name)));
    }

    @Override
    public Mono<List<User<ID>>> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User<ID>> updateUser(ID id, String name, Role role) {
        return getUserByName(name)
                .flatMap(existingUser -> {
                    if (!existingUser.getId().equals(id)){
                        return Mono.error(new InvalidValueException("Invalid name: there's another user with that name."));
                    }
                    return Mono.empty();
                })
                .onErrorResume(UserNotFoundException.class, e -> Mono.empty())
                .then(Mono.defer(() -> getUserById(id)))
                .flatMap(user -> {
                    if (name != null) user.setName(name);
                    if (role != null) user.setRole(role);
                    return saveUser(user);
                });
    }

}
