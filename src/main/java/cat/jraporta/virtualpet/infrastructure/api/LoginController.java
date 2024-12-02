package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@RestController
public class LoginController {

    private final UserServiceAdapter userServiceAdapter;

    @PostMapping("api/users")
    public Mono<ResponseEntity<Long>> registerUser(@RequestBody UserDto userDto){
        log.debug("createUser with body: {}", userDto);
        return userServiceAdapter.saveUser(userDto)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping("api/users")
    public Mono<ResponseEntity<UserDto>> login(@RequestBody UserDto userDto){
        log.debug("getUser with name: {}", userDto.getName());
        return userServiceAdapter.getUserByName(userDto.getName())
                .map(ResponseEntity::ok);
    }


}
