package cat.jraporta.virtualpet.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class PropertiesRetriever {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

}
