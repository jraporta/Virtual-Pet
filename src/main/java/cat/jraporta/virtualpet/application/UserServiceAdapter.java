package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.both.UserDto;
import cat.jraporta.virtualpet.application.dto.request.UserUpdateRequest;
import cat.jraporta.virtualpet.application.mapper.UserDtoMapper;
import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.port.in.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceAdapter {

    private UserService<String> userService;
    private UserDtoMapper userDtoMapper;

    public Mono<String> saveUser(UserDto userDto){
        return userService.saveUser(userDtoMapper.toDomain(userDto))
                .map(User::getId);
    }

    public Mono<UserDto> getUserByName(String name){
        return userService.getUserByUsername(name)
                .map(userDtoMapper::toDto);
    }

    public Mono<List<UserDto>> getAllUsers() {
        return userService.getAllUsers()
                .map(users -> users.stream()
                            .map(userDtoMapper::toDto)
                            .toList()
                );
    }

    public Mono<UserDto> updateUser(UserUpdateRequest request) {
        return userService.updateUser(request.getId(), request.getName(), request.getRole())
                .map(userDtoMapper::toDto);
    }
}
