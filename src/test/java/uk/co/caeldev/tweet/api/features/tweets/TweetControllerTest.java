package uk.co.caeldev.tweet.api.features.tweets;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static uk.co.caeldev.tweet.api.features.tweets.TweetBuilder.tweetBuilder;
import static uk.co.caeldev.tweet.api.features.tweets.TweetResourceBuilder.tweetResourceBuilder;
import static uk.co.caeldev.tweet.api.features.users.UserBuilder.userBuilder;
import static uk.org.fyodor.generators.RDG.*;

@RunWith(MockitoJUnitRunner.class)
public class TweetControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private TweetService tweetService;

    @Mock
    private UserService userService;

    @Mock
    private TweetResourceSupport tweetResourceSupport;

    private TweetController tweetController;

    @Before
    public void testee() throws Exception {
        tweetController = new TweetController(tweetService, userService, tweetResourceSupport);
    }

    @Test
    public void shouldPostTweetAndCreateUserWhenUserGUIDIsNotPresent() throws Exception {
        //Given
        final String message = string().next();
        final User expectedUser = userBuilder().build();
        final Tweet expectedTweet = tweetBuilder()
                .message(message)
                .user(expectedUser)
                .build();

        //And
        given(userService.findOrCreateByGUID(null))
                .willReturn(expectedUser);

        //And
        given(tweetService.postTweet(message, expectedUser))
                .willReturn(expectedTweet);

        //And
        given(tweetResourceSupport.toResource(expectedTweet))
                .willReturn(tweetResourceBuilder()
                        .tweet(expectedTweet)
                        .userGUID(expectedUser.getUserGUID())
                        .build());

        //When
        final ResponseEntity<TweetResource> response = tweetController.createTweet(message, null);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final TweetResource body = response.getBody();
        assertThat(body.getUserGUID()).isEqualTo(expectedUser.getUserGUID());
        assertThat(body.getTweetGUID()).isEqualTo(expectedTweet.getTweetGUID());
        assertThat(body.getMessage()).isEqualTo(expectedTweet.getMessage());
    }

    @Test
    public void shouldPostTweetWhenUserGUIDIsPresent() throws Exception {
        //Given
        final String message = string().next();
        final UUID userGUID = UUID.randomUUID();

        final User expectedUser = userBuilder()
                .userGUID(userGUID)
                .build();

        final Tweet expectedTweet = tweetBuilder()
                .message(message)
                .user(expectedUser)
                .build();

        //And
        given(userService.findOrCreateByGUID(userGUID))
                .willReturn(expectedUser);

        //And
        given(tweetService.postTweet(message, expectedUser))
                .willReturn(expectedTweet);

        //And
        given(tweetResourceSupport.toResource(expectedTweet))
                .willReturn(tweetResourceBuilder()
                        .tweet(expectedTweet)
                        .userGUID(expectedUser.getUserGUID())
                        .build());

        //When
        final ResponseEntity<TweetResource> response = tweetController.createTweet(message, userGUID);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        final TweetResource body = response.getBody();
        assertThat(body.getUserGUID()).isEqualTo(expectedUser.getUserGUID());
        assertThat(body.getTweetGUID()).isEqualTo(expectedTweet.getTweetGUID());
        assertThat(body.getMessage()).isEqualTo(expectedTweet.getMessage());
    }

    @Test
    public void shouldNotPostTweetWhenUserGUIDIsPresentAndNotValid() throws Exception {
        //Given
        final String message = string().next();
        final UUID userGUID = UUID.randomUUID();

        //And
        when(userService.findOrCreateByGUID(userGUID)).thenThrow(IllegalArgumentException.class);

        //Expect
        exception.expect(IllegalArgumentException.class);

        //When
        tweetController.createTweet(message, userGUID);

        //Then
        verifyZeroInteractions(tweetService);
    }

    @Test
    public void shouldGetAllUserTweets() {
        // Given
        final UUID userGUID = UUID.randomUUID();

        //And
        final User expectedUser = userBuilder().userGUID(userGUID).build();
        given(userService.findOrCreateByGUID(userGUID)).willReturn(expectedUser);

        //And
        final List<Tweet> expectedTweets = list(() -> new Tweet(longVal().next(),
                string().next(),
                expectedUser.getId(),
                UUID.randomUUID(),
                LocalDateTime.now())).next();

        given(tweetService.getTweetsByUserId(expectedUser.getId()))
                .willReturn(expectedTweets);
        // When
        final ResponseEntity<List<TweetResource>> tweets = tweetController.getTweets(userGUID);

        // Then
        assertThat(tweets.getBody()).hasSameSizeAs(expectedTweets);
    }
}
