package cat.jraporta.virtualpet.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {

    String user;

    String password;

}
