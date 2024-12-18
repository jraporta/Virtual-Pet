package cat.jraporta.virtualpet.application;

import cat.jraporta.virtualpet.application.dto.request.PetUpdateRequest;
import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.application.dto.enums.Action;
import cat.jraporta.virtualpet.core.port.in.PetCareService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        if (name != null) pet.setName(name);
        if (color != null) pet.setColor(color);
        return Mono.just(pet);
    }
}
