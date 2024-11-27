package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.PetDtoMapper;
import cat.jraporta.virtualpet.application.dto.PetDto;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.port.in.PetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PetServiceAdapter {

    private PetService<Long> petService;
    private PetDtoMapper petDtoMapper;

    public Mono<Long> savePet(PetDto petDto){
        return petService.savePet(petDtoMapper.toDomain(petDto))
                .map(Pet::getId);
    }

    public Mono<PetDto> getPetById(Long id){
        return petService.getPetById(id)
                .map(petDtoMapper::toDto);
    }

}
