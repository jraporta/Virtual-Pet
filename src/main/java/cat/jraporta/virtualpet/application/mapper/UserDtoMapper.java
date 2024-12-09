package cat.jraporta.virtualpet.application.mapper;

import cat.jraporta.virtualpet.application.dto.both.PetDto;
import cat.jraporta.virtualpet.application.dto.both.UserDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {

    PetDtoMapper petMapper;

    public UserDtoMapper(PetDtoMapper petMapper) {
        this.petMapper = petMapper;
    }

    public UserDto toDto(User<Long> user){
        List<PetDto> pets = user.getPets().stream()
                .map(petMapper::toDto)
                .toList();
        return new UserDto(user.getId(), user.getName(), user.getRole(), pets);
    }

    public User<Long> toDomain(UserDto userDto){
        List<Pet<Long>> pets = userDto.getPets().stream()
                .map(petMapper::toDomain)
                .toList();
        return new User<>(userDto.getId(), userDto.getName(), null, userDto.getRole(), pets);
    }

}
