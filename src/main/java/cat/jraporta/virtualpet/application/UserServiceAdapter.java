package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.response.UserDto;
import cat.jraporta.virtualpet.application.dto.request.UserUpdateRequest;
import cat.jraporta.virtualpet.application.mapper.UserDtoMapper;
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

    public Mono<UserDto> getUserByName(String name){
        return userService.getUserByName(name)
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
