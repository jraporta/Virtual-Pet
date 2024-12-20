package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.both.UserDto;
import cat.jraporta.virtualpet.application.mapper.UserDtoMapper;
import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.in.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class UserServiceAdapter {

    private UserService<Long> userService;
    private UserDtoMapper userDtoMapper;

    public Mono<Long> saveUser(UserDto userDto){
        return userService.saveUser(userDtoMapper.toDomain(userDto))
                .map(User::getId);
    }

    public Mono<UserDto> getUserByName(String name){
        return userService.getUserByUsername(name)
                .map(userDtoMapper::toDto);
    }

}
