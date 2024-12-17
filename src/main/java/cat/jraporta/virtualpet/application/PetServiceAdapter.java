package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.request.PetCreationRequest;
import cat.jraporta.virtualpet.application.dto.request.PetUpdateRequest;
import cat.jraporta.virtualpet.application.mapper.PetDtoMapper;
import cat.jraporta.virtualpet.application.dto.response.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Location;
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

    private PetService<String> petService;
    private PetDtoMapper petDtoMapper;

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

    public Mono<PetDto> updatePet(PetUpdateRequest petData) {
        return petService.getPetById(petData.getId())
                .flatMap(pet -> {
                    if (petData.getName() != null) pet.setName(petData.getName());
                    if (petData.getColor() != null) pet.setColor(petData.getColor());
                    if (petData.getHasPoo() != null && !petData.getHasPoo()) pet.resetPooCountdown();
                    if (petData.getLocation() != null) pet.setLocation(Location.valueOf(petData.getLocation()));
                    if (petData.getAccessories() != null) pet.setAccessories(petData.getAccessories());
                    return petService.updatePet(pet);
                })
                .map(petDtoMapper::toDto);
    }

    public Mono<Void> checkOwnershipOfPet(String username, String petId) {
        return petService.checkOwnershipOfPet(username, petId);
    }

    public Mono<Void> deletePet(String id) {
        return petService.deletePet(id);
    }
}
