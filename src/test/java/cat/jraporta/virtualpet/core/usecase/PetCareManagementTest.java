package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.PetFactory;
import cat.jraporta.virtualpet.core.domain.enums.*;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetCareManagementTest {

    @InjectMocks
    @Spy
    private PetCareManagement<String> petCareManagement;

    @Mock
    private PetRepository<String> petRepository;

    @Mock
    private Pet<String> mockPet;

    private static final PetFactory<String> petFactory = new PetFactory<>();
    private static Pet<String> feetTestPet1, testPet, testPet2;
    private static final Map<Food, Integer> energyChangesAfterFeeding = new HashMap<>();
    private static boolean feedTestExecuted = false;
    private static final Map<Accessory, Integer> happinessChangesAfterChangingAccessory = new HashMap<>();
    private static boolean accessoryTestExecuted = false;
    private static final Map<Location, Integer> happinessChangesAfterChangingLocation = new HashMap<>();
    private static boolean locationTestExecuted = false;

    @BeforeAll
    static void setUp() {
        testPet = petFactory.createPet("TestPet", Type.CAT, "blue", "1234L").block();
        testPet2 = petFactory.createPet("TestPet", Type.CAT, "blue", "1234L").block();
    }

    @BeforeAll
    static void createHungryPet() {
        feetTestPet1 = petFactory.createPet("TestPet", Type.CAT, "blue", "1234L").block();
        feetTestPet1.getEnergy().reset();
    }

    @ParameterizedTest
    @EnumSource(Food.class)
    void feedPet_ShouldIncreasePetEnergyUpTo20Points(Food food) {
        feedTestExecuted = true;
        int initialEnergy = feetTestPet1.getEnergy().getValue();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        assertTrue(initialEnergy < 80, "Energy too high to perform feeding test: " + initialEnergy);

        StepVerifier.create(petCareManagement.feedPet(feetTestPet1, food))
                .assertNext(updatedPet -> {
                    int energyChange = updatedPet.getEnergy().getValue() - initialEnergy;

                    energyChangesAfterFeeding.put(food, energyChange);

                    assertTrue(energyChange >= 0 && energyChange <= 20,
                            () -> "Energy change should be between 0 and 20, but was " + energyChange);
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(feetTestPet1);
    }

    @AfterAll
    static void validateFoodEffects() {
        if(feedTestExecuted) {
            AtomicBoolean zeroChangeFound = new AtomicBoolean(false);
            AtomicBoolean maxBoostFound = new AtomicBoolean(false);

            System.out.println("\nEnergy Change Results:");
            energyChangesAfterFeeding.forEach((food, change) -> {
                System.out.println(food + " -> Energy Change: " + change);
                if (change == 0) zeroChangeFound.set(true);
                if (change == 20) maxBoostFound.set(true);
            });

            assertTrue(zeroChangeFound.get(), "At least one food should result in no energy change");
            assertTrue(maxBoostFound.get(), "At least one food should increase energy by exactly 20");
        }
    }


    @ParameterizedTest
    @EnumSource(Food.class)
    void feedPet_EnergyStaysBellow100(Food food) {
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(Flux.range(1, 5)
                        .flatMap(i -> petCareManagement.feedPet(testPet, food))
                        .doOnNext(updatedPet -> {
                            int petEnergy = updatedPet.getEnergy().getValue();
                            assertTrue( petEnergy <= 100,
                                    () -> "Energy should stay bellow 100 but was " + petEnergy);
                        }))
                .expectNextCount(5)
                .verifyComplete();
    }

    @ParameterizedTest
    @EnumSource(Accessory.class)
    void updateAccessory_ShouldChangeHappiness(Accessory accessory) {
        accessoryTestExecuted = true;
        int initialHappiness = testPet.getHappiness().getValue();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        assertTrue(initialHappiness < 70 && initialHappiness > 30, "Happiness range invalid to perform test");

        StepVerifier.create(petCareManagement.updateAccessory(testPet, accessory))
                .assertNext(updatedPet -> {
                    int happinessChange = updatedPet.getHappiness().getValue() - initialHappiness;

                    happinessChangesAfterChangingAccessory.put(accessory, happinessChange);

                    assertTrue(happinessChange >= -17 && happinessChange <= 17,
                            () -> "Happiness change should be between -15 and 15, but was " + happinessChange);
                    assertTrue(updatedPet.getAccessories().contains(accessory),
                            () -> "Pet should wear the accessory " + accessory);
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(testPet);

        StepVerifier.create(petCareManagement.updateAccessory(testPet, accessory))
                .assertNext(updatedPet -> {
                    int happinessChange = initialHappiness - updatedPet.getHappiness().getValue();

                    assertTrue(happinessChange <= 1 && happinessChange >= 0,
                            () -> "Happiness should be restored to the previous value " + happinessChange);
                    assertFalse(updatedPet.getAccessories().contains(accessory),
                            () -> "Pet should not wear the accessory " + accessory);
                })
                .verifyComplete();
    }

    @AfterAll
    static void validateAccessoryEffects() {
        if(accessoryTestExecuted) {
            AtomicBoolean negativeChangeFound = new AtomicBoolean(false);
            AtomicBoolean positiveChangeFound = new AtomicBoolean(false);

            System.out.println("\nHappiness Change Results:");
            happinessChangesAfterChangingAccessory.forEach((accessory, change) -> {
                System.out.println(accessory + " -> Happiness Change: " + change);
                if (change <= -10) negativeChangeFound.set(true);
                if (change >= 10) positiveChangeFound.set(true);
            });

            assertTrue(negativeChangeFound.get(), "At least one accessory should increase happiness");
            assertTrue(positiveChangeFound.get(), "At least one accessory should decrease happiness");
        }
    }

    @ParameterizedTest
    @EnumSource(Location.class)
    void updateLocation_ShouldChangeHappiness(Location location) {
        Pet<String> pet = testPet2;
        locationTestExecuted = true;
        int baseHappiness = 50;
        int initialHappiness = pet.getHappiness().getValue();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        assertTrue(initialHappiness < 80 && initialHappiness > 20, "Happiness range invalid to perform test: " + initialHappiness);

        StepVerifier.create(petCareManagement.updateLocation(pet, location))
                .assertNext(updatedPet -> {
                    int happinessChange = updatedPet.getHappiness().getValue() - baseHappiness;

                    happinessChangesAfterChangingLocation.put(location, happinessChange);

                    assertTrue(happinessChange >= -32 && happinessChange <= 32,
                            () -> "Happiness change should be between -30 and 30, but was " + happinessChange);
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @AfterAll
    static void validateLocationEffects() {
        if(locationTestExecuted) {
            AtomicBoolean negativeChangeFound = new AtomicBoolean(false);
            AtomicBoolean positiveChangeFound = new AtomicBoolean(false);

            System.out.println("\nHappiness Change Results:");
            happinessChangesAfterChangingLocation.forEach((location, change) -> {
                System.out.println(location + " -> Happiness Change: " + change);
                if (change <= -10) negativeChangeFound.set(true);
                if (change >= 10) positiveChangeFound.set(true);
            });

            assertTrue(negativeChangeFound.get(), "At least one location should increase happiness");
            assertTrue(positiveChangeFound.get(), "At least one location should decrease happiness");
        }
    }

    @Test
    void playWithPet_ShouldIncreaseHappinessAndDecreaseEnergy() {
        Pet<String> pet = petFactory.createPet("TestPet", Type.CAT, "blue", "1234L").block();
        int initialHappiness = pet.getHappiness().getValue();
        int initialEnergy = pet.getEnergy().getValue();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        assertTrue(initialHappiness < 90, "Happiness too high to perform test: " + initialHappiness);
        assertTrue(initialEnergy > 10, "Energy too low to perform test: " + initialEnergy);

        StepVerifier.create(petCareManagement.playWithPet(pet))
                .assertNext(updatedPet -> {
                    int happinessChange = updatedPet.getHappiness().getValue() - initialHappiness;
                    int energyChange = updatedPet.getEnergy().getValue() - initialEnergy;
                    assertTrue(happinessChange >= 4 && happinessChange <= 5,
                            () -> "Happiness change should be around 5, but was " + happinessChange);
                    assertTrue(energyChange >= -6 && energyChange <= -5,
                            () -> "Happiness change should be around -5, but was " + happinessChange);
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void cleanPoo_ShouldCallCleanPooAndSavePet() {
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petCareManagement.cleanPoo(mockPet))
                .expectNext(mockPet)
                .verifyComplete();

        verify(mockPet, times(1)).cleanPoo();
        verify(petRepository, times(1)).savePet(mockPet);
    }

    @Test
    void updatePetData_ShouldUpdateNameAndColor_WhenBothAreNotNull() {
        Pet<String> pet = testPet;
        String newName = "Draco";
        String newColor = "purple";
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petCareManagement.updatePetData(pet, newName, newColor))
                .assertNext(updatedPet -> {
                    assertTrue(newName.equalsIgnoreCase(updatedPet.getName()),
                            () -> "Name should have been updated");
                    assertTrue(newColor.equalsIgnoreCase(updatedPet.getColor()),
                            () -> "Color should have been updated");
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void updatePetData_ShouldUpdateName_WhenColorIsNull() {
        Pet<String> pet = testPet;
        String newName = "Draco";
        String newColor = null;
        String oldColor = testPet.getColor();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petCareManagement.updatePetData(pet, newName, newColor))
                .assertNext(updatedPet -> {
                    assertTrue(newName.equalsIgnoreCase(updatedPet.getName()),
                            () -> "Name should have been updated");
                    assertTrue(oldColor.equalsIgnoreCase(updatedPet.getColor()),
                            () -> "Color should stay the same");
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void updatePetData_ShouldUpdateColor_WhenNameIsNull() {
        Pet<String> pet = testPet;
        String newName = null;
        String newColor = "purple";
        String oldName = testPet.getName();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petCareManagement.updatePetData(pet, newName, newColor))
                .assertNext(updatedPet -> {
                    assertTrue(oldName.equalsIgnoreCase(updatedPet.getName()),
                            () -> "Name should stay the same");
                    assertTrue(newColor.equalsIgnoreCase(updatedPet.getColor()),
                            () -> "Color should have been updated");
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void updatePetData_ShouldNotUpdateNameAndColor_WhenBothAreNull() {
        Pet<String> pet = testPet;
        String newName = null;
        String newColor = null;
        String oldName = testPet.getName();
        String oldColor = testPet.getColor();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petCareManagement.updatePetData(pet, newName, newColor))
                .assertNext(updatedPet -> {
                    assertTrue(oldName.equalsIgnoreCase(updatedPet.getName()),
                            () -> "Name should have been updated");
                    assertTrue(oldColor.equalsIgnoreCase(updatedPet.getColor()),
                            () -> "Color should have been updated");
                })
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }
}