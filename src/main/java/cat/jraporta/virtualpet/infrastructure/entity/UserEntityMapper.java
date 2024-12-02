package cat.jraporta.virtualpet.infrastructure.entity;

import cat.jraporta.virtualpet.core.domain.User;

public class UserEntityMapper {

    public UserEntity toEntity(User<Long> user){
        return new UserEntity(user.getId(), user.getName(), user.getPassword(), user.getRole());
    }

    public User<Long> toDomain(UserEntity userEntity){
        return new User<>(userEntity.getId(), userEntity.getName(), userEntity.getPassword(), userEntity.getRole());
    }

}
