package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.dto.request.SignInRequest;
import cat.jraporta.virtualpet.application.dto.request.SignUpRequest;
import cat.jraporta.virtualpet.application.dto.response.JwtAuthenticationResponse;
import cat.jraporta.virtualpet.infrastructure.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Account Management", description = "Endpoints for login and registration of users")
@AllArgsConstructor
@Slf4j
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary ="Register",
            description = "User registration.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignUpRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Username already exists",
                                            value = "Invalid username: there is already an user with the name Nobita"
                                    ),
                                    @ExampleObject(
                                            name = "Missing username",
                                            value = "User is a required field"
                                    ),
                                    @ExampleObject(
                                            name = "Missing password",
                                            value = "Password is a required field"
                                    )
                            }
                    ))
            }
    )
    @PostMapping("api/register")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signUp(@Validated @RequestBody SignUpRequest request){
        log.debug("Sign up request: {}", request.getUser());
        return authenticationService.signUp(request)
                .doOnNext(unused -> log.debug("Register successful for {}, sent token", request.getUser()))
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(token))
                .doOnError(error -> log.warn("Registration failed for user: {}, error: {}",
                        request.getUser(), error.getMessage()));
    }

    @Operation(
            summary ="Login",
            description = "User Login.",
            requestBody =
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User credentials",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SignInRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged successfully", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Missing username",
                                            value = "User is a required field"
                                    ),
                                    @ExampleObject(
                                            name = "Missing password",
                                            value = "Password is a required field"
                                    )
                            }
                    ))
            }
    )
    @PostMapping("api/login")
    public Mono<ResponseEntity<JwtAuthenticationResponse>> signIn(@Validated @RequestBody SignInRequest request){
        log.debug("Sign in request: {}", request.getUser());
        return authenticationService.signIn(request)
                .doOnNext(unused -> log.debug("Login successful for {}, sent token", request.getUser()))
                .map(ResponseEntity::ok)
                .doOnError(error -> log.warn("Login failed for user: {}, error: {}",
                        request.getUser(), error.getMessage()));
    }


}
