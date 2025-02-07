package cat.jraporta.virtualpet.core.usecase;

import cat.jraporta.virtualpet.core.domain.User;
import cat.jraporta.virtualpet.core.domain.enums.Role;
import cat.jraporta.virtualpet.core.exceptions.InvalidValueException;
import cat.jraporta.virtualpet.core.exceptions.UserNotFoundException;
import cat.jraporta.virtualpet.core.port.out.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainUserServiceTest {

    @InjectMocks
    private DomainUserService<String> userService;

    @Mock
    private UserRepository<String> userRepository;

    @Test
    void saveUser_ShouldReturnSavedUser() {
        User<String> user = new User<>(null, "testUser", "1234", Role.USER, null);
        when(userRepository.saveUser(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userService.saveUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    void saveUser_ShouldHandleError() {
        User<String> user = new User<>(null, "testUser", "1234", Role.USER, null);
        when(userRepository.saveUser(user)).thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(userService.saveUser(user))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        String userId = "123";
        User<String> user = new User<>(userId, "testUser", "1234", Role.USER, null);
        when(userRepository.findById(userId)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getUserById(userId))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_ShouldReturnErrorWhenUserNotFound() {
        String userId = "123";
        when(userRepository.findById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserById(userId))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals("No user found with id: " + userId))
                .verify();

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByName_ShouldReturnUser() {
        String userName = "testUser";
        User<String> user = new User<>("123", userName, "1234", Role.USER, null);
        when(userRepository.findByName(userName)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getUserByName(userName))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByName(userName);
    }

    @Test
    void getUserByName_ShouldReturnErrorWhenUserNotFound() {
        String userName = "testUser";
        when(userRepository.findByName(userName)).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserByName(userName))
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().equals("No user found with name: " + userName))
                .verify();

        verify(userRepository, times(1)).findByName(userName);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User<String>> users = List.of(
                new User<>("123", "user1", "1234", Role.USER, null),
                new User<>("124", "user1", "1234", Role.USER, null)
        );
        when(userRepository.findAll()).thenReturn(Mono.just(users));

        StepVerifier.create(userService.getAllUsers())
                .expectNext(users)
                .verifyComplete();

        verify(userRepository, times(1)).findAll();
    }

    @ParameterizedTest
    @MethodSource("provideUpdateUserTestCases")
    void updateUser_ShouldUpdateUserSuccessfully(String newName, Role newRole) {
        String userId = "123";
        User<String> userToUpdate = new User<>(userId, "oldName", "1234", Role.USER, null);
        User<String> updatedUser = new User<>(
                userId,
                Objects.requireNonNullElse(newName, userToUpdate.getName()),
                "1234",
                Objects.requireNonNullElse(newRole, userToUpdate.getRole()),
                null
        );

        when(userRepository.findByName(newName)).thenReturn(Mono.empty());
        when(userRepository.findById(userId)).thenReturn(Mono.just(userToUpdate));
        when(userRepository.saveUser(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userService.updateUser(userId, newName, newRole))
                .expectNext(updatedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByName(newName);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).saveUser(any());
    }

    static Stream<Arguments> provideUpdateUserTestCases() {
        return Stream.of(
                Arguments.of("updatedUser", Role.ADMIN),   // Both name and role are provided
                Arguments.of(null, Role.ADMIN),            // Name is null, role is provided
                Arguments.of("updatedUser", null)          // Name is provided, role is null
        );
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfullyIfUsernameDoesNotChange() {
        String userId = "123";
        String oldName = "oldName";
        Role newRole = Role.ADMIN;
        User<String> userToUpdate = new User<>(userId, oldName, "1234", Role.USER, null);
        User<String> updatedUser = new User<>(userId, oldName, "1234", newRole, null);

        when(userRepository.findByName(oldName)).thenReturn(Mono.just(userToUpdate));
        when(userRepository.findById(userId)).thenReturn(Mono.just(userToUpdate));
        when(userRepository.saveUser(any())).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        StepVerifier.create(userService.updateUser(userId, oldName, newRole))
                .expectNext(updatedUser)
                .verifyComplete();

        verify(userRepository, times(1)).findByName(oldName);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).saveUser(any());
    }

    @Test
    void updateUser_ShouldReturnExceptionWhenThereIsAlreadyUserWithUsername() {
        String userId = "123";
        String newName = "updatedUser";
        Role newRole = Role.ADMIN;
        User<String> anotherUser = new User<>("222", newName, "1234", Role.USER, null);

        when(userRepository.findByName(newName)).thenReturn(Mono.just(anotherUser));

        StepVerifier.create(userService.updateUser(userId, newName, newRole))
                .expectErrorMatches(throwable -> throwable instanceof InvalidValueException &&
                        throwable.getMessage().equals("Invalid name: there's another user with that name."))
                .verify();

        verify(userRepository, times(1)).findByName(newName);
    }
}