package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.core.port.in.PetService;
import cat.jraporta.virtualpet.infrastructure.IdMapper;
import cat.jraporta.virtualpet.infrastructure.PetEntity;
import cat.jraporta.virtualpet.infrastructure.PetMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PetServiceAdapter {

    private PetService petService;
    private PetMapper petMapper;
    private IdMapper idMapper;

    public Mono<String> savePet(PetEntity petEntity){
        return petService.savePet(petMapper.mapToPet(petEntity))
                .map(pet -> idMapper.mapToLong(pet.getPetId()).toString());
    }

    public Mono<PetEntity> getPetById(Long id){
        return petService.getPetById(idMapper.mapToPetId(id))
                .map(petMapper::mapToPetEntity);
    }

}
