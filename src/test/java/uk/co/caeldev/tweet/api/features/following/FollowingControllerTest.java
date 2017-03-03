package uk.co.caeldev.tweet.api.features.following;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.caeldev.tweet.api.features.tweets.Tweet;
import uk.co.caeldev.tweet.api.features.tweets.TweetResource;
import uk.co.caeldev.tweet.api.features.tweets.TweetResourceSupport;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static uk.co.caeldev.tweet.api.features.users.UserBuilder.userBuilder;
import static uk.org.fyodor.generators.RDG.*;

@RunWith(MockitoJUnitRunner.class)
public class FollowingControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private FollowingService followingService;

    @Mock
    private UserService userService;

    @Mock
    private TweetResourceSupport tweetResourceSupport;

    private FollowingController followingController;

    @Before
    public void testee() {
        followingController = new FollowingController(followingService, userService, tweetResourceSupport);
    }
    
    @Test
    public void shouldFollowAUser() {
        // Given
        final UUID followingUserGUID = UUID.randomUUID();
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedFollowingUser = userBuilder().userGUID(followingUserGUID).build();
        given(userService.findByGUID(followingUserGUID)).willReturn(Optional.of(expectedFollowingUser));

        //And
        final User expectedUser = userBuilder().userGUID(userGUID).build();
        given(userService.findByGUID(userGUID)).willReturn(Optional.of(expectedUser));

        // When
        final ResponseEntity<Following> result = followingController.follow(followingUserGUID, userGUID);

        // Then
        verify(followingService).follow(expectedFollowingUser, expectedUser);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldNotFollowAUserWhenUserGUIDIsInvalid() {
        // Given
        final UUID followingUserGUID = UUID.randomUUID();
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedFollowingUser = userBuilder().userGUID(followingUserGUID).build();
        given(userService.findByGUID(followingUserGUID)).willReturn(Optional.of(expectedFollowingUser));

        // And
        given(userService.findByGUID(userGUID)).willReturn(Optional.empty());

        // Expect
        exception.expect(IllegalArgumentException.class);

        // When
        followingController.follow(followingUserGUID, userGUID);

        // Then
        verifyZeroInteractions(followingService);
    }

    @Test
    public void shouldNotFollowAUserWhenFollowingUserGUIDIsInvalid() {
        // Given
        final UUID followingUserGUID = UUID.randomUUID();
        final UUID userGUID = UUID.randomUUID();

        // And
        given(userService.findByGUID(followingUserGUID)).willReturn(Optional.empty());

        // Expect
        exception.expect(IllegalArgumentException.class);

        // When
        followingController.follow(followingUserGUID, userGUID);

        // Then
        verifyZeroInteractions(followingService);
    }

    @Test
    public void shouldGetTimeline() throws Exception {
        //Given
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedUser = userBuilder().userGUID(userGUID).build();
        given(userService.findByGUID(userGUID)).willReturn(Optional.of(expectedUser));

        //And
        final List<Tweet> expectedTweets = list(() -> new Tweet(longVal().next(),
                string().next(),
                longVal().next(),
                UUID.randomUUID(),
                LocalDateTime.now())).next();
        given(followingService.getTimeline(expectedUser.getId()))
                .willReturn(expectedTweets);

        //When
        final ResponseEntity<List<TweetResource>> results = followingController.timeline(userGUID);

        //Then
        assertThat(results.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(results.getBody()).hasSameSizeAs(expectedTweets);
    }

    @Test
    public void shouldNotGetTimelineForInvalidUserGUID() throws Exception {
        //Given
        final UUID userGUID = UUID.randomUUID();

        //And
        given(userService.findByGUID(userGUID)).willReturn(Optional.empty());

        //Expect
        exception.expect(IllegalArgumentException.class);

        //When
        followingController.timeline(userGUID);
    }
}
