package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;

import java.util.Date;

public class PetNeedsUpdateService <ID> {

    public void refresh(Pet<ID> pet) {
        long millis = System.currentTimeMillis() - pet.getUpdateDate().getTime();

    }

    private void updateHappiness(Pet<ID> pet, long millis) {
        final int RATE = 1000;
        int decreaseQty = (int) millis / RATE;
        int value = pet.getHappiness() - decreaseQty;
        pet.setHappiness(Math.max(value, 0));
    }

    private void updateEnergy(Pet<ID> pet, long millis) {
        final int RATE = 1000;
        int decreaseQty = (int) millis / RATE;
        int value = pet.getEnergy() - decreaseQty;
        pet.setEnergy(Math.max(value, 0));
    }

    private void updateMood(Pet<ID> pet, long millis) {
    }

    private void updateIsAsleep(Pet<ID> pet, long millis) {
    }

    private void updatePooCountdown(Pet<ID> pet, long millis) {
    }

    private void updateUpdateDate(Pet<ID> pet) {
        pet.setUpdateDate(new Date(System.currentTimeMillis()));
    }
}
