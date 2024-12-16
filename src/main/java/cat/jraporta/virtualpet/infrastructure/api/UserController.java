package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.both.UserDto;
import cat.jraporta.virtualpet.application.dto.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "User Management", description = "Endpoints for managing users")
@AllArgsConstructor
@Slf4j
@RestController
public class UserController {

    private final UserServiceAdapter userServiceAdapter;

    @Operation(
            summary ="Get a User",
            description = "Gets the details of the User specified in the path.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
                                            value = "No user found with this id"
                                    )})),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
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


    @Operation(
            summary ="Get all the Users",
            description = "Gets a list of all the users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
                    ))
            }
    )
    @GetMapping("admin/users")
    public Mono<ResponseEntity<List<UserDto>>> getAllUsers(){
        log.debug("Get all users");
        return userServiceAdapter.getAllUsers()
                .map(ResponseEntity::ok);
    }


    @Operation(
            summary ="Update a User",
            description = "updates a User's data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "User not found",
                                            value = "No user found with this id"
                                    )})),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
                    ))
            }
    )
    @PutMapping("admin/users")
    public Mono<ResponseEntity<UserDto>> updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data of the pet",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateRequest.class)))
            @RequestBody UserUpdateRequest request
    ){
        log.debug("Update user with id: {}", request.getId());
        return userServiceAdapter.updateUser(request)
                .map(ResponseEntity::ok);
    }

}
