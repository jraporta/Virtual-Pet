package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.User;

import java.util.List;

public class UserEntityMapper {

    private final PetEntityMapper petMapper;

    public UserEntityMapper(PetEntityMapper petMapper) {
        this.petMapper = petMapper;
    }

    public UserEntity toEntity(User<String> user){
        List<PetEntity> pets = user.getPets().stream()
                .map(petMapper::toEntity)
                .toList();
        return new UserEntity(Long.valueOf(user.getId()), user.getName(), user.getPassword(), user.getRole(), pets);
    }

    public User<String> toDomain(UserEntity userEntity){
        List<Pet<String>> pets = userEntity.getPets().stream()
                .map(petMapper::toDomain)
                .toList();
        return new User<>(userEntity.getId().toString(), userEntity.getName(), userEntity.getPassword(), userEntity.getRole(), pets);
    }

}
