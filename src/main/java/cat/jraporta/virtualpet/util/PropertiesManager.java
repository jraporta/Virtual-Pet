package cat.jraporta.virtualpet.util;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class PropertiesManager {

    //Problem fetching the property value results in the impossibility to create the class bean.
    // Check the application.properties file to track the origin of the problem.

    @Value("${token.signing.key}")
    private String jwtSigningKey;

}
