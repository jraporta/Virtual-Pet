package cat.jraporta.virtualpet.core.port.in;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import cat.jraporta.virtualpet.core.domain.enums.Food;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import reactor.core.publisher.Mono;

public interface PetCareService<ID> {
    
    Mono<Pet<ID>> feedPet(Pet<ID> pet, Food food);
    Mono<Pet<ID>> updateAccessory(Pet<ID> pet, Accessory accessory);
    Mono<Pet<ID>> updateLocation(Pet<ID> pet, Location location);
    Mono<Pet<ID>> playWithPet(Pet<ID> pet);
    Mono<Pet<ID>> cleanPoo(Pet<ID> pet);

}
