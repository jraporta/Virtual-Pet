package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.both.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "User Management", description = "Endpoints for managing users")
@AllArgsConstructor
@Slf4j
@RestController
public class UserController {

    private final UserServiceAdapter userServiceAdapter;

    @Operation(
            summary ="Get a User",
            description = "Gets the details of the user specified in the path.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    ))
            }
    )
    @GetMapping("api/users/{name}")
    public Mono<ResponseEntity<UserDto>> getUser(
            @Parameter(description = "Name of the User", example = "666")
            @PathVariable String name
    ){
        log.debug("getUser with name: {}", name);
        return userServiceAdapter.getUserByName(name)
                .map(ResponseEntity::ok);
    }

}
