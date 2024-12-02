package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@RestController
public class UserController {

    private final UserServiceAdapter userServiceAdapter;

    @GetMapping("api/users/{name}")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable String name){
        log.debug("getUser with name: {}", name);
        return userServiceAdapter.getUserByName(name)
                .map(ResponseEntity::ok);
    }

}
