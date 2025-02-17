package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.request.PetCreationRequest;
import cat.jraporta.virtualpet.application.dto.request.PetUpdateRequest;
import cat.jraporta.virtualpet.application.dto.mapper.PetDtoMapper;
import cat.jraporta.virtualpet.application.dto.response.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import cat.jraporta.virtualpet.core.port.in.PetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Service
public class PetServiceAdapter {

    private final PetService<String> petService;
    private final PetDtoMapper petDtoMapper;
    private final PetUpdateServiceAdapter petUpdateService;

    public Mono<String> createPet(PetCreationRequest request, String userId){
        return petService.createPet(request.getName(), Type.valueOf(request.getType()), request.getColor(), userId)
                .map(Pet::getId);
    }

    public Mono<PetDto> getPetById(String id){
        return petService.getPetById(id)
                .map(petDtoMapper::toDto);
    }

    public Flux<PetDto> getAllThePets() {
        return petService.getAllPets()
                .map(petDtoMapper::toDto);
    }

    public Mono<List<PetDto>> getAllPetsOfUser(String name) {
        return petService.getAllPetsOfUser(name)
                .map(pets -> pets.stream()
                        .map(petDtoMapper::toDto)
                        .toList());
    }

    public Mono<PetDto> updatePet(PetUpdateRequest request) {
        return petService.getPetById(request.getId())
                .flatMap(pet -> petUpdateService.handleAction(request, pet))
                .map(petDtoMapper::toDto);
    }

    public Mono<Void> checkOwnershipOfPet(String username, String petId) {
        return petService.checkOwnershipOfPet(username, petId);
    }

    public Mono<Void> deletePet(String id) {
        return petService.deletePet(id);
    }
}
