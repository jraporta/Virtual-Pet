package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.Pet;
import cat.jraporta.virtualpet.core.domain.PetFactory;
import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.core.domain.enums.Type;
import cat.jraporta.virtualpet.core.port.out.PetRepository;
import cat.jraporta.virtualpet.infrastructure.exception.UnauthorizedActionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainPetServiceTest {

    @InjectMocks
    @Spy
    private DomainPetService<String> petService;

    @Mock
    private PetRepository<String> petRepository;

    @Mock
    private DomainUserService<String> userService;

    @Mock
    private PetFactory<String> mockPetFactory;

    @Mock
    private Pet<String> mockPet1;

    @Mock
    private Pet<String> mockPet2;

    @Test
    void createPet_ShouldReturnCreatedPet() {
        PetFactory<String> petFactory = new PetFactory<>();
        String name = "TestPet";
        Type type = Type.CAT;
        String color = "red";
        String userId = "1234";
        Pet<String> pet = petFactory.createPet(name, type, color, userId).block();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(mockPetFactory.createPet(name, type, color, userId)).thenReturn(Mono.just(pet));

        StepVerifier.create(petService.createPet(name, type, color, userId))
                .expectNext(pet)
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void savePet_ShouldReturnSavedPet() {
        PetFactory<String> petFactory = new PetFactory<>();
        Pet<String> pet = petFactory.createPet("testPet", Type.CAT, "red", "1234").block();
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petService.savePet(pet))
                .expectNext(pet)
                .verifyComplete();

        verify(petRepository, times(1)).savePet(pet);
    }

    @Test
    void getPetById_ShouldRefreshAndReturnPet() {
        String id = "1234F";
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));
        when(petRepository.findById(id)).thenReturn(Mono.just(mockPet1));

        StepVerifier.create(petService.getPetById(id))
                .expectNext(mockPet1)
                .verifyComplete();

        verify(petRepository, times(1)).findById(id);
        verify(mockPet1, times(1)).refresh();
        verify(petRepository, times(1)).savePet(mockPet1);
    }

    @Test
    void getPetById_ShouldReturnEmptyMonoWhenPetNotFound() {
        String id = "1234F";
        when(petRepository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(petService.getPetById(id))
                .expectNextCount(0)
                .verifyComplete();

        verify(petRepository, times(1)).findById(id);
    }

    @Test
    void getAllPetsOfUser_ShouldRefreshAndReturnUsersPets() {
        String username = "testUser";
        List<Pet<String>> pets = List.of(mockPet1, mockPet2);
        User<String> user = new User<>("123", username, "1234", Role.USER, pets);
        when(userService.getUserByName(username)).thenReturn(Mono.just(user));
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petService.getAllPetsOfUser(username))
                .expectNext(pets)
                .verifyComplete();

        verify(mockPet1, times(1)).refresh();
        verify(petRepository, times(1)).savePet(mockPet1);
        verify(mockPet2, times(1)).refresh();
        verify(petRepository, times(1)).savePet(mockPet2);
    }

    @Test
    void getAllPets_ShouldRefreshAndReturnAllPets() {
        List<Pet<String>> pets = List.of(mockPet1, mockPet2);
        when(petRepository.findAll()).thenReturn(Flux.fromIterable(pets));
        when(petRepository.savePet(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(petService.getAllPets())
                .expectNextMatches(pet -> pet == mockPet1) // Ensures reference matches
                .expectNextMatches(pet -> pet == mockPet2)
                .verifyComplete();

        verify(mockPet1, times(1)).refresh();
        verify(petRepository, times(1)).savePet(mockPet1);
        verify(mockPet2, times(1)).refresh();
        verify(petRepository, times(1)).savePet(mockPet2);
    }

    @Test
    void checkOwnershipOfPet_ShouldCompleteIfOwnershipProved() {
        String username = "TestUser";
        String petId = "1234F";
        PetFactory<String> petFactory = new PetFactory<>();
        String name = "TestPet";
        Type type = Type.CAT;
        String color = "red";
        String userId = "22F4";
        Pet<String> pet = petFactory.createPet(name, type, color, userId).block();
        User<String> user = new User<>(userId, username, "1234", Role.USER, List.of(pet, mockPet1));
        when(userService.getUserByName(username)).thenReturn(Mono.just(user));
        doReturn(Mono.just(pet)).when(petService).getPetById(petId);

        StepVerifier.create(petService.checkOwnershipOfPet(username, petId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void checkOwnershipOfPet_ShouldReturnErrorWhenOwnershipTestFails() {
        String username = "TestUser";
        String petId = "1234F";
        PetFactory<String> petFactory = new PetFactory<>();
        String name = "TestPet";
        Type type = Type.CAT;
        String color = "red";
        String userId = "22F4";
        Pet<String> pet = petFactory.createPet(name, type, color, "C562").block();
        User<String> user = new User<>(userId, username, "1234", Role.USER, List.of(pet, mockPet1));
        when(userService.getUserByName(username)).thenReturn(Mono.just(user));
        doReturn(Mono.just(pet)).when(petService).getPetById(petId);

        StepVerifier.create(petService.checkOwnershipOfPet(username, petId))
                .expectErrorMatches(throwable -> throwable instanceof UnauthorizedActionException &&
                        throwable.getMessage().equals("Unauthorized action: Only the pet owner is allowed to perform this operation."))
                .verify();
    }

    @Test
    void deletePet_ShouldDeletePetFromRepository() {
        String petId = "1234F";
        when(petRepository.deletePet(any())).thenReturn(Mono.empty());

        StepVerifier.create(petService.deletePet(petId))
                .expectNextCount(0)
                .verifyComplete();

        verify(petRepository, times(1)).deletePet(petId);
    }
}