package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.infrastructure.PetEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
public class PetController {

    private PetServiceAdapter petServiceAdapter;

    @PostMapping("api/create")
    public Mono<ResponseEntity<String>> createPet(@RequestBody PetEntity petEntity){
        return petServiceAdapter.savePet(petEntity)
                .map(ResponseEntity::ok);
    }

    @GetMapping("api/pets/{id}")
    public Mono<ResponseEntity<PetEntity>> getPet(@PathVariable Long id){
        return petServiceAdapter.getPetById(id)
                .map(ResponseEntity::ok);
    }


}
