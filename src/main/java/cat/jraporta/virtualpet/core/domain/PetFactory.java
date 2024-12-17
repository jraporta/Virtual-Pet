package cat.jraporta.virtualpet.core.domain;

import cat.jraporta.virtualpet.core.domain.enums.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class PetFactory<ID> {

    public Mono<Pet<ID>> createPet(String name, Type type, String color, ID userId) {

        return Mono.just(
                Pet.<ID>builder()
                        .name(name)
                        .userId(userId)
                        .type(type)
                        .color(color)
                        .happiness(50)
                        .energy(50)
                        .mood(Mood.HAPPY)
                        .isAsleep(false)
                        .hasPoo(false)
                        .location(Location.ROOM)
                        .accessories(new HashSet<>())
                        .accessoryPreferences(createPreferences(Accessory.class, -15, 15))
                        .locationPreferences(createPreferences(Location.class, -20, 20))
                        .foodPreferences(createPreferences(Food.class, 0, 20))
                        .updateDate(new Date(System.currentTimeMillis()))
                        .build()
        );
    }

    private <T extends Enum<T>> Map<T, Integer> createPreferences(Class<T> enumClass, int min, int max) {
        Map<T, Integer> preferences = new HashMap<>();
        List<T> elements = new ArrayList<>(Arrays.stream(enumClass.getEnumConstants()).toList());
        Collections.shuffle(elements);
        int n = elements.size();
        int range = max - min;
        for (int i = 0; i < n; i++){
            int value = Math.round(min + (range * ((float) i / (n - 1))));
            preferences.put(elements.get(i), value);
        }
        return preferences;
    }


}
