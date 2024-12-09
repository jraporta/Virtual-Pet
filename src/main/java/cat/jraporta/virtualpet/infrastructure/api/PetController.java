package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.PetServiceAdapter;
import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.both.PetDto;
import cat.jraporta.virtualpet.application.dto.both.UserDto;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.infrastructure.exception.UnauthorizedActionException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Tag(name = "Pet Management", description = "Endpoints for managing pets")
@AllArgsConstructor
@Slf4j
@RestController
public class PetController {

    private final PetServiceAdapter petServiceAdapter;
    private final UserServiceAdapter userServiceAdapter;



    @Operation(
            summary ="Create a new Pet",
            description = "Creates a new Pet. Ownership of the pet is determined via the JWT token.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pet created", content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    name = "Id of the newly created Pet",
                                    value = "57"
                    ))),
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
    @PostMapping("api/pets")
    public Mono<ResponseEntity<Long>> createPet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data of the pet",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetDto.class)))
            @RequestBody PetDto petDto
    ){
        log.debug("createPet with body: {}", petDto);
        return getUserId()
                .flatMap(id -> {
                    petDto.setUserId(id);
                    return petServiceAdapter.savePet(petDto);
                })
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }



    @Operation(
            summary ="Get a Pet",
            description = "Gets the details of the Pet specified in the path.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pet found", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetDto.class)
                    )),
                    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Pet not found",
                                            value = "No pet found with this id"
                                    )})),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )}
                    ))
            }
    )
    @GetMapping("api/pets/{id}")
    public Mono<ResponseEntity<PetDto>> getPet(
            @Parameter(description = "Id of the Pet", example = "57")
            @PathVariable Long id
    ){
        log.debug("getPet with id: {}", id);
        return petServiceAdapter.getPetById(id)
                .map(ResponseEntity::ok);
    }



    @Operation(
            summary ="Get all the Pets",
            description = "Gets a list of the Pet belonging to the User.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of pets", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PetDto.class))
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
    @GetMapping("api/pets")
    public Mono<ResponseEntity<List<PetDto>>> getAllPetsOfUser(){
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    String name = securityContext.getAuthentication().getName();
                    log.debug("Get all the Pets of user: {}", name);
                    return petServiceAdapter.getAllPetsOfUser(name);
                })
                .map(ResponseEntity::ok);
    }



    @Operation(
            summary ="Get the Pets of all the Users",
            description = "Gets a list of all the Pets in the database.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of pets", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PetDto.class))
                    )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized action",
                                            value = "Unauthorized action: This operation is restricted to users with the Admin role."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
                    ))
            }
    )
    @GetMapping("admin/pets")
    public Mono<ResponseEntity<List<PetDto>>> getAllThePets(){
        log.debug("get all the Pets request");
        return isAdmin()
                .thenMany(petServiceAdapter.getAllThePets())
                .collectList()
                .map(ResponseEntity::ok);
    }



    @Operation(
            summary ="Update an existing Pet",
            description = "Updates an existing Pet.",
            responses = {
                    @ApiResponse(responseCode = "20", description = "Updated Pet", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetDto.class)
                            )),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized action",
                                            value = "Unauthorized action: Only the pet owner is allowed to perform this operation."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
                    ))
            }
    )
    @PutMapping("api/pets")
    public Mono<ResponseEntity<PetDto>> updatePet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data of the pet",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PetDto.class)))
            @RequestBody PetDto petDto
    ){
        log.debug("update Pet with id: {}", petDto.getId());
        return checkOwnershipOfPet(petDto.getId())
                .flatMap(unused -> petServiceAdapter.updatePet(petDto))
                .map(ResponseEntity::ok);
    }



    @Operation(
            summary ="Delete a Pet",
            description = "Deletes the Pet specified in the path.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Pet deleted", content = @Content()),
                    @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Pet not found",
                                            value = "No pet found with this id"
                                    )})),
                    @ApiResponse(responseCode = "401", description = "Unauthorized action", content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Unauthorized action",
                                            value = "Unauthorized action: Only the pet owner is allowed to perform this operation."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid JWT",
                                            value = "Invalid JWT: token is expired"
                                    )
                            }
                    ))
            }
    )
    @DeleteMapping("api/pets/{id}")
    public Mono<ResponseEntity<Void>> deletePet(
            @Parameter(description = "Id of the Pet", example = "57")
            @PathVariable Long id
    ){
        log.debug("delete Pet with id: {}", id);
        return checkOwnershipOfPet(id)
                .then(petServiceAdapter.deletePet(id))
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }

    private Mono<Void> checkOwnershipOfPet(Long petId){
        return isAdmin().
                onErrorResume(unused -> ReactiveSecurityContextHolder.getContext()
                        .flatMap(securityContext -> {
                            String username = securityContext.getAuthentication().getName();
                            return petServiceAdapter.checkOwnershipOfPet(username, petId);
                        }));
    }

    private Mono<Void> isAdmin(){
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    if (securityContext.getAuthentication().getAuthorities().contains(Role.ADMIN.name())){
                        return Mono.empty();
                    }
                    return Mono.error(new UnauthorizedActionException("Unauthorized action: This operation is restricted to users with the Admin role."));
                });
    }

    //TODO: store user id in token
    private Mono<Long> getUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    String username = securityContext.getAuthentication().getName();
                    return userServiceAdapter.getUserByName(username)
                            .map(UserDto::getId);
                });
    }

}
