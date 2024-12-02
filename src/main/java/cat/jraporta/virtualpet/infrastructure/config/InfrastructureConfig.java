package cat.jraporta.virtualpet.infrastructure.config;

import cat.jraporta.virtualpet.infrastructure.entity.PetEntityMapper;
import cat.jraporta.virtualpet.infrastructure.entity.UserEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfig {

    @Bean
    public PetEntityMapper getPetEntityMapper(){
        return new PetEntityMapper();
    }

    @Bean
    public UserEntityMapper getUserEntityMapper(){
        return new UserEntityMapper();
    }

}
