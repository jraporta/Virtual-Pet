package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@RestController
public class LoginController {

    private UserServiceAdapter userServiceAdapter;

    @PostMapping("api/user")
    public Mono<ResponseEntity<Long>> createUser(@RequestBody UserDto userDto){
        log.debug("createUser with body: {}", userDto);
        return userServiceAdapter.saveUser(userDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("api/user/{id}")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable String name){
        log.debug("getUser with name: {}", name);
        return userServiceAdapter.getUserByName(name)
                .map(ResponseEntity::ok);
    }


}
