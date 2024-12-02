package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.in.UserService;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class DomainUserService<ID> implements UserService<ID> {

    UserRepository<ID> userRepository;

    @Override
    public Mono<User<ID>> saveUser(User<ID> user) {
        log.debug("save user: {}", user);
        return userRepository.saveUser(user);
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username).block();
    }
}
