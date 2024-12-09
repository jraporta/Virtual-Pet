package cat.jraporta.virtualpet.infrastructure.api;

import cat.jraporta.virtualpet.application.PetServiceAdapter;
import cat.jraporta.virtualpet.application.UserServiceAdapter;
import cat.jraporta.virtualpet.application.dto.PetDto;
import cat.jraporta.virtualpet.application.dto.UserDto;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.infrastructure.exception.UnauthorizedActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
public class PetController {

    private final PetServiceAdapter petServiceAdapter;
    private final UserServiceAdapter userServiceAdapter;

    @PostMapping("api/pets")
    public Mono<ResponseEntity<Long>> createPet(@RequestBody PetDto petDto){
        log.debug("createPet with body: {}", petDto);
        return getUserId()
                .flatMap(id -> {
                    petDto.setUserId(id);
                    return petServiceAdapter.savePet(petDto);
                })
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }

    @GetMapping("api/pets/{id}")
    public Mono<ResponseEntity<PetDto>> getPet(@PathVariable Long id){
        log.debug("getPet with id: {}", id);
        return petServiceAdapter.getPetById(id)
                .map(ResponseEntity::ok);
    }

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


    @GetMapping("admin/pets")
    public Mono<ResponseEntity<List<PetDto>>> getAllThePets(){
        log.debug("get all the Pets request");
        return isAdmin()
                .thenMany(petServiceAdapter.getAllThePets())
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PutMapping("api/pets")
    public Mono<ResponseEntity<PetDto>> updatePet(@RequestBody PetDto petDto){
        log.debug("update Pet with id: {}", petDto.getId());
        return checkOwnershipOfPet(petDto.getId())
                .flatMap(unused -> petServiceAdapter.updatePet(petDto))
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("api/pets/{id}")
    public Mono<ResponseEntity<Void>> deletePet(@PathVariable Long id){
        log.debug("delete Pet with id: {}", id);
        return checkOwnershipOfPet(id)
                .flatMap(unused -> petServiceAdapter.deletePet(id))
                .map(unused -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
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
                    return Mono.error(new UnauthorizedActionException("Not Authorized action"));
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
