package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.PetDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@RestController
public class PetController {

    private PetServiceAdapter petServiceAdapter;

    @PostMapping("api/create")
    public Mono<ResponseEntity<Long>> createPet(@RequestBody PetDto petDto){
        log.debug("createPet with body: {}", petDto);
        return petServiceAdapter.savePet(petDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("api/pets/{id}")
    public Mono<ResponseEntity<PetDto>> getPet(@PathVariable Long id){
        log.debug("getPet with id: {}", id);
        return petServiceAdapter.getPetById(id)
                .map(ResponseEntity::ok);
    }


}
