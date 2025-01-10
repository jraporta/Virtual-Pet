package cat.jraporta.virtualpet.core.port.out;

import cat.jraporta.virtualpet.core.domain.User;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserRepository<ID> {

    Mono<User<ID>> saveUser(User<ID> user);
    Mono<User<ID>> findById(ID id);
    Mono<User<ID>> findByName(String name);
    Mono<List<User<ID>>> findAll();
}
