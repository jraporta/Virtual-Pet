package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.*;
import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ToString
@Getter
@AllArgsConstructor
@Builder
public class Pet<ID> {

    private ID id;
    private String name;
    private ID userId;
    private Type type;
    private String color;
    private Stat happiness;
    private Stat energy;
    private Mood mood;
    private boolean isAsleep;
    private boolean isDead;
    private boolean hasPoo;
    private Stat pooUrge;
    private Location location;
    private Set<Accessory> accessories;
    private Map<Accessory, Integer> accessoryPreferences;
    private Map<Location, Integer> locationPreferences;
    private Map<Food, Integer> foodPreferences;
    private Date lastInteractionDate;
    private Date updateDate;

    public void refresh() {
        long millisSinceLastUpdate = System.currentTimeMillis() - this.updateDate.getTime();
        this.updateDate = new Date(System.currentTimeMillis());
        dropEnergy(millisSinceLastUpdate);
        dropHappiness(millisSinceLastUpdate);
        checkPoo();
        checkAsleep();
        checkMood();
    }

    public void changeLocation(Location location) {
        interactWithPet();
        int oldHappiness = this.locationPreferences.getOrDefault(this.location, 0);
        int newHappiness = this.locationPreferences.getOrDefault(location, 0);
        this.happiness.increaseStat(newHappiness - oldHappiness);
        this.location = location;
    }

    public void toggleAccessory(Accessory accessory) {
        interactWithPet();
        int delta = this.accessoryPreferences.getOrDefault(accessory, 0);
        boolean isRemoved = this.accessories.remove(accessory);
        if (!isRemoved) this.accessories.add(accessory);
        this.happiness.increaseStat(isRemoved ? -delta : delta);
    }

    public void cleanPoo() {
        interactWithPet();
        this.hasPoo = false;
    }

    public void play() {
        interactWithPet();
        this.happiness.increaseStat(5);
        this.energy.increaseStat(-5);
    }

    private void checkMood() {
        if (this.energy.getValue() < 40) {
            this.mood = Mood.HUNGRY;
        }else if (this.happiness.getValue() < 30) {
            this.mood = Mood.SAD;
        }else if (this.happiness.getValue() > 95) {
            this.mood = Mood.EXCITED;
        } else {
            this.mood = Mood.HAPPY;
        }
    }

    private void checkAsleep() {
        if (this.isAsleep) {
            if (this.energy.getValue() >= 100) {
                this.isAsleep = false;
            }
            return;
        }

        if (this.energy.getValue() < 25 && Math.random() < 0.2) {
            this.isAsleep = true;
        }
    }

    private void checkPoo() {
        if (this.isAsleep || this.hasPoo) {
            return;
        }

        if (this.pooUrge.getValue() > 90 && Math.random() < 0.2) {
            this.hasPoo = true;
            this.pooUrge.reset();
        }
    }

    public void feed(Food food) {
        interactWithPet();
        int delta = this.foodPreferences.getOrDefault(food,5);
        this.energy.increaseStat(delta);
        this.pooUrge.increaseStat(10);
    }

    private void dropEnergy(long millis) {
        long value = this.isAsleep ? millis / 500 : -millis / 1000;
        this.energy.increaseStat(value);
    }

    private void dropHappiness(long millis) {
        long value = this.isAsleep ? 0 : -millis / 2000;
        this.happiness.increaseStat(value);
    }

    private void interactWithPet(){
        refresh();
        this.isAsleep = false;
        this.lastInteractionDate = new Date(System.currentTimeMillis());
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pet<?> pet)) return false;
        return Objects.equals(id, pet.id) && Objects.equals(name, pet.name) && Objects.equals(userId, pet.userId) && type == pet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, type);
    }
}
