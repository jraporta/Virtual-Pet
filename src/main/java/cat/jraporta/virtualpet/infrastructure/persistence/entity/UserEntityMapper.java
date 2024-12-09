package cat.jraporta.virtualpet.infrastructure.persistence.entity;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.User;

import java.util.List;

public class UserEntityMapper {

    PetEntityMapper petMapper;

    public UserEntityMapper(PetEntityMapper petMapper) {
        this.petMapper = petMapper;
    }

    public UserEntity toEntity(User<Long> user){
        List<PetEntity> pets = user.getPets().stream()
                .map(petMapper::toEntity)
                .toList();
        return new UserEntity(user.getId(), user.getName(), user.getPassword(), user.getRole(), pets);
    }

    public User<Long> toDomain(UserEntity userEntity){
        List<Pet<Long>> pets = userEntity.getPets().stream()
                .map(petMapper::toDomain)
                .toList();
        return new User<>(userEntity.getId(), userEntity.getName(), userEntity.getPassword(), userEntity.getRole(), pets);
    }

}
