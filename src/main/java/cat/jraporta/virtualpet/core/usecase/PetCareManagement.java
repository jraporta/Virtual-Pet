package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.enums.Accessory;
import cat.jraporta.virtualpet.core.domain.enums.Food;
import cat.jraporta.virtualpet.core.domain.enums.Location;
import cat.jraporta.virtualpet.core.domain.enums.Mood;
import cat.jraporta.virtualpet.core.port.in.PetCareService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class PetCareManagement<ID> implements PetCareService<ID> {

    public void refresh(Pet<ID> pet) {
        long millis = System.currentTimeMillis() - pet.getUpdateDate().getTime();
        pet.setUpdateDate(new Date(System.currentTimeMillis()));
        dropEnergy(pet, millis);
        dropHappiness(pet, millis);
        dropHunger(pet, millis);
        checkPoo(pet, millis);
        checkAsleep(pet);
        checkMood(pet);
    }

    private void checkMood(Pet<ID> pet) {
        Mood mood;
        if (pet.getHunger() < 20) {
            mood = Mood.HUNGRY;
        }else if (pet.getHappiness() < 30) {
            mood = Mood.SAD;
        }else if (pet.getHappiness() > 95) {
            mood = Mood.EXCITED;
        } else {
            mood = Mood.HAPPY;
        }
    }

    private void checkAsleep(Pet<ID> pet) {
        if (pet.isAsleep()) {
            if (pet.getEnergy() > 100) {
                pet.setAsleep(false);
            }
            return;
        }

        if (pet.getEnergy() < 25 && Math.random() < 0.2) {
            pet.setAsleep(true);
        }
    }

    private void checkPoo(Pet<ID> pet, long millis) {
        if (pet.isAsleep() || pet.isHasPoo()) {
            return;
        }

        if (pet.getPooUrge() > 90 && Math.random() < 0.2) {
            pet.setHasPoo(true);
            pet.setPooUrge(0);
        }
    }

    private void dropHunger(Pet<ID> pet, long millis) {
        long value = pet.isAsleep() ? -millis / 5000 : -millis / 1500;
        increaseStat(pet::setHunger, pet::getHunger, value);
    }

    private void dropHappiness(Pet<ID> pet, long millis) {
        long value = pet.isAsleep() ? 0 : -millis / 2000;
        increaseStat(pet::setHappiness, pet::getHappiness, value);
    }

    private void dropEnergy(Pet<ID> pet, long millis) {
        long value = pet.isAsleep() ? millis / 500 : -millis / 1000;
        increaseStat(pet::setEnergy, pet::getEnergy, value);
    }

    private void interactWithPet(Pet<ID> pet){
        refresh(pet);
        pet.setAsleep(false);
        pet.setLastInteractionDate(new Date(System.currentTimeMillis()));
    }

    private void increaseStat(Consumer<Integer> setter, Supplier<Integer> getter, int quantity) {
        int newValue = Math.max(0, Math.min(100, getter.get() + quantity));
        setter.accept(newValue);
    }

    private void increaseStat(Consumer<Integer> setter, Supplier<Integer> getter, long quantity) {
        increaseStat(setter, getter, (int) quantity);
    }

    @Override
    public Mono<Pet<ID>> feedPet(Pet<ID> pet, Food food) {
        interactWithPet(pet);
        int delta = pet.getFoodPreferences().getOrDefault(food,5);
        increaseStat(pet::setHunger, pet::getHunger, delta);
        increaseStat(pet::setPooUrge, pet::getPooUrge, 10);
        if (pet.getHunger() < 100) {
            increaseStat(pet::setEnergy, pet::getEnergy, delta);
        }
        return Mono.just(pet);
    }

    @Override
    public Mono<Pet<ID>> updateAccessory(Pet<ID> pet, Accessory accessory) {
        interactWithPet(pet);
        int delta = pet.getAccessoryPreferences().getOrDefault(accessory, 0);
        boolean isRemoved = pet.getAccessories().remove(accessory);
        if (!isRemoved) pet.getAccessories().add(accessory);
        increaseStat(pet::setHappiness, pet::getHappiness, isRemoved ? -delta : delta);
        return Mono.just(pet);
    }

    @Override
    public Mono<Pet<ID>> updateLocation(Pet<ID> pet, Location location) {
        interactWithPet(pet);
        int oldHappiness = pet.getLocationPreferences().getOrDefault(pet.getLocation(), 0);
        int newHappiness = pet.getLocationPreferences().getOrDefault(location, 0);
        increaseStat(pet::setHappiness, pet::getHappiness, newHappiness - oldHappiness);
        pet.setLocation(location);
        return Mono.just(pet);
    }

    @Override
    public Mono<Pet<ID>> playWithPet(Pet<ID> pet) {
        interactWithPet(pet);
        increaseStat(pet::setHappiness, pet::getHappiness, 8);
        increaseStat(pet::setEnergy, pet::getEnergy, -5);
        increaseStat(pet::setHunger, pet::getHunger, -5);
        return Mono.just(pet);
    }

    @Override
    public Mono<Pet<ID>> cleanPoo(Pet<ID> pet) {
        interactWithPet(pet);
        pet.setHasPoo(false);
        return Mono.just(pet);
    }
}
