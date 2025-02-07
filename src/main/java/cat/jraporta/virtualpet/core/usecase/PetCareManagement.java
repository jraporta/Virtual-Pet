package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import cat.jraporta.virtualpet.core.domain.enums.Food;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.port.in.PetCareService;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class PetCareManagement<ID> implements PetCareService<ID> {

    private PetRepository<ID> petRepository;

    @Override
    public Mono<Pet<ID>> feedPet(Pet<ID> pet, Food food) {
        pet.feed(food);
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> updateAccessory(Pet<ID> pet, Accessory accessory) {
        pet.toggleAccessory(accessory);
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> updateLocation(Pet<ID> pet, Location location) {
        pet.changeLocation(location);
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> playWithPet(Pet<ID> pet) {
        pet.play();
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> cleanPoo(Pet<ID> pet) {
        pet.cleanPoo();
        return petRepository.savePet(pet);
    }

    @Override
    public Mono<Pet<ID>> updatePetData(Pet<ID> pet, String name, String color) {
        log.debug("Pet data update starting...");
        if (name != null) {
            log.debug("Update pet name {}", name);
            pet.updateName(name);
        }
        if (color != null) {
            log.debug("Update pet color {}", color);
            pet.updateColor(color);
        }
        return petRepository.savePet(pet);
    }

}
