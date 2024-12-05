package cat.jraporta.virtualpet.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class PropertiesManager {

    //Error creating bean stems from problem fetching the property value. Check the application.properties file

    @Value("${token.signing.key}")
    private String jwtSigningKey;

}
