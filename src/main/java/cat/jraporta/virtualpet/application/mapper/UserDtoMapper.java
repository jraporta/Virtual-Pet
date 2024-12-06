package cat.jraporta.virtualpet.application.mapper;

import cat.jraporta.virtualpet.application.dto.UserDto;
import cat.jraporta.virtualpet.core.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public UserDto toDto(User<Long> user){
        return new UserDto(user.getId(), user.getName(), user.getPassword(), user.getRole());
    }

    public User<Long> toDomain(UserDto userDto){
        return new User<>(userDto.getId(), userDto.getName(), userDto.getPassword(), userDto.getRole());
    }

}
