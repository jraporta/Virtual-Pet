package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.User;
import reactor.core.publisher.Mono;

public interface UserService<ID> {

    Mono<User<ID>> saveUser(User<ID> user);
    Mono<User<ID>> getUserById(ID id);
    Mono<User<ID>> getUserByUsername(String username);

}
