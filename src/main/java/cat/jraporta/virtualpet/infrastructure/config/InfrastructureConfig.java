package cat.jraporta.virtualpet.infrastructure.config;

import cat.jraporta.virtualpet.infrastructure.persistence.entity.PetEntityMapper;
import cat.jraporta.virtualpet.infrastructure.persistence.entity.UserEntityMapper;
import cat.jraporta.virtualpet.util.PropertiesManager;
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

    @Bean
    public PropertiesManager propertiesRetriever(){
        return new PropertiesManager();
    }

}
