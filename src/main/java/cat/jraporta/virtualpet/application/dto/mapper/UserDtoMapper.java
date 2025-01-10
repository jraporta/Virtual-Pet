package cat.jraporta.virtualpet.application.dto.mapper;

import cat.jraporta.virtualpet.application.dto.response.PetDto;
import cat.jraporta.virtualpet.application.dto.response.UserDto;
import cat.jraporta.virtualpet.core.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {

    PetDtoMapper petMapper;

    public UserDtoMapper(PetDtoMapper petMapper) {
        this.petMapper = petMapper;
    }

    public UserDto toDto(User<String> user){
        List<PetDto> pets = user.getPets().stream()
                .map(petMapper::toDto)
                .toList();
        return new UserDto(user.getId(), user.getName(), user.getRole(), pets);
    }

}
