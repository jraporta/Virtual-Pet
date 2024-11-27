package cat.jraporta.virtualpet.application.config;

import cat.jraporta.virtualpet.application.dto.PetDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PetDtoMapper getPetDtoMapper(){
        return new PetDtoMapper();
    }

}
