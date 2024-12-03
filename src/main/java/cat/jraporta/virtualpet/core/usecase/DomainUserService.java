package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.User;
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
        return userRepository.saveUser(user)
                .doOnNext(savedUser -> log.debug("User saved: {}", savedUser));
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
