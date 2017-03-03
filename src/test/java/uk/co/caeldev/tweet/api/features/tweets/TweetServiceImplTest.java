package uk.co.caeldev.tweet.api.features.tweets;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.caeldev.tweet.api.features.users.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.tweet.api.features.tweets.TweetBuilder.tweetBuilder;
import static uk.co.caeldev.tweet.api.features.users.UserBuilder.userBuilder;
import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceImplTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private TweetRepository tweetRepository;
    private TweetService tweetService;

    @Before
    public void testee() throws Exception {
        this.tweetService = new TweetServiceImpl(tweetRepository);
    }

    @Test
    public void shouldPostTweetAValidTweet() throws Exception {
        //Given
        final String message = string(140).next();
        final User user = userBuilder().build();

        //When
        tweetService.postTweet(message, user);

        //Then
        verify(tweetRepository).save(any(Tweet.class));
    }

    @Test
    public void shouldNotPostTweetWhenMessageHasMore140() throws Exception {
        //Given
        final String message = string(150).next();
        final User user = userBuilder().build();

        //Expect
        exception.expect(IllegalArgumentException.class);

        //When
        tweetService.postTweet(message, user);
    }

    @Test
    public void shouldGetAllTweetsByUserIdReverseChronologicalOrder() {
        // Given
        final Long userId = longVal().next();

        //And
        final Tweet first = tweetBuilder()
                .creationDate(LocalDateTime.now())
                .build();
        final Tweet second = tweetBuilder()
                .creationDate(LocalDateTime.now().minusDays(1))
                .build();
        final Tweet third = tweetBuilder()
                .creationDate(LocalDateTime.now().minusDays(4))
                .build();
        final List<Tweet> expectedTweets = Lists.newArrayList(
                third,
                first,
                second);
        given(tweetRepository.getAllByUserId(userId)).willReturn(expectedTweets);

        // When
        final List<Tweet> tweetsByUserId = tweetService.getTweetsByUserId(userId);

        // Then
        assertThat(tweetsByUserId).hasSize(3);
        assertThat(tweetsByUserId.get(0)).isEqualTo(first);
        assertThat(tweetsByUserId.get(1)).isEqualTo(second);
        assertThat(tweetsByUserId.get(2)).isEqualTo(third);
    }

}