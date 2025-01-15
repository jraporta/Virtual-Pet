package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.request.PetUpdateRequest;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.application.dto.enums.Action;
import cat.jraporta.virtualpet.core.port.in.PetCareService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class PetUpdateServiceAdapter {

    private final PetCareService<String> petCareService;


    public Mono<Pet<String>> handleAction(PetUpdateRequest request, Pet<String> pet) {
        return switch(request.getAction()) {
            case Action.CLEAN -> petCareService.cleanPoo(pet);
            case Action.FEED -> petCareService.feedPet(pet, request.getFood());
            case Action.PLAY -> petCareService.playWithPet(pet);
            case Action.LOCATION -> petCareService.updateLocation(pet, request.getLocation());
            case Action.ACCESSORY -> petCareService.updateAccessory(pet, request.getAccessory());
            case Action.DATA -> updatePet(pet, request.getName(), request.getColor());
        };
    }

    private Mono<Pet<String>> updatePet(Pet<String> pet, String name, String color) {
        log.debug("Pet data update starting...");
        if (name != null) {
            log.debug("Update pet name {}", name);
            pet.setName(name);
        }
        if (color != null) {
            log.debug("Update pet color {}", color);
            pet.setColor(color);
        }
        return Mono.just(pet);
    }
}
