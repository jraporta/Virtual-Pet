package cat.jraporta.virtualpet.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PropertiesRetriever {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

}
