package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService<ID> {

    Mono<User<ID>> saveUser(User<ID> user);
    Mono<User<ID>> getUserById(ID id);
    Mono<User<ID>> getUserByUsername(String username);
    Mono<List<User<ID>>> getAllUsers();
    Mono<User<ID>> updateUser(ID id, String name, Role role);
}
