package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.request.PetCreationRequest;
import cat.jraporta.virtualpet.application.mapper.PetDtoMapper;
import cat.jraporta.virtualpet.application.dto.both.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Species;
import cat.jraporta.virtualpet.core.port.in.PetService;
import cat.jraporta.virtualpet.core.usecase.PetCreationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Service
public class PetServiceAdapter {

    private PetCreationService<String> petCreationService;
    private PetService<String> petService;
    private PetDtoMapper petDtoMapper;

    public Mono<String> createPet(PetCreationRequest request, String userId){
        return petCreationService.createPet(request.getName(), Species.valueOf(request.getSpecies()), request.getColor(), userId)
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

    public Mono<PetDto> updatePet(PetDto petDto) {
        return petService.updatePet(petDtoMapper.toDomain(petDto))
                .map(petDtoMapper::toDto);
    }

    public Mono<Void> checkOwnershipOfPet(String username, String petId) {
        return petService.checkOwnershipOfPet(username, petId);
    }

    public Mono<Void> deletePet(String id) {
        return petService.deletePet(id);
    }
}
