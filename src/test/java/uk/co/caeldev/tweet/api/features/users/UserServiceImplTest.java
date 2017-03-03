package uk.co.caeldev.tweet.api.features.users;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.tweet.api.features.users.UserBuilder.userBuilder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void testee() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void shouldFindExistingUser() {
        // Given
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedUser = userBuilder().userGUID(userGUID).build();
        given(userRepository.findUserByUUID(userGUID))
                .willReturn(Optional.of(expectedUser));

        // When
        final User result = userService.findOrCreateByGUID(userGUID);

        // Then
        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    public void shouldCreateUserWhenUserUUIDIsNull() {
        //Given
        final UUID userGUID = UUID.randomUUID();

        //And
        given(userRepository.findUserByUUID(userGUID))
                .willReturn(Optional.empty());

        //And
        final User expectedUser = userBuilder().build();
        given(userRepository.create())
                .willReturn(expectedUser);

        //When
        final User result = userService.findOrCreateByGUID(null);

        //Then
        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    public void shouldNotCreateUserWhenUserUUIDIsNotNullAndNotRegistered() {
        //Given
        final UUID userGUID = UUID.randomUUID();

        //And
        given(userRepository.findUserByUUID(userGUID))
                .willReturn(Optional.empty());

        //Expect
        exception.expect(IllegalArgumentException.class);

        //When
        userService.findOrCreateByGUID(userGUID);

        //Then
        verify(userRepository, never()).create();
    }

    @Test
    public void shouldFindUserByGUID() {
        // Given
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedUser = userBuilder()
                .userGUID(userGUID)
                .build();
        given(userRepository.findUserByUUID(userGUID))
                .willReturn(Optional.of(expectedUser));

        // When
        final Optional<User> user = userService.findByGUID(userGUID);

        // Then
        assertThat(user.isPresent()).isEqualTo(true);
    }

}