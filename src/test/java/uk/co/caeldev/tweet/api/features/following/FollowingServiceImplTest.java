package uk.co.caeldev.tweet.api.features.following;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.caeldev.tweet.api.features.tweets.Tweet;
import uk.co.caeldev.tweet.api.features.tweets.TweetRepository;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.tweet.api.features.following.FollowingBuilder.followingBuilder;
import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class FollowingServiceImplTest {

    @Mock
    private FollowingRepository followingRepository;

    @Mock
    private TweetRepository tweetRepository;

    private FollowingService followingService;

    @Before
    public void testee() throws Exception {
        followingService = new FollowingServiceImpl(followingRepository, tweetRepository);
    }

    @Test
    public void shouldFollowAUser() throws Exception {
        //Given
        final User followingUser = UserBuilder.userBuilder().build();
        final User user = UserBuilder.userBuilder().build();

        //And
        given(followingRepository.findEntry(user.getId(), followingUser.getId()))
                .willReturn(Optional.empty());

        final ArgumentCaptor<Following> argumentCaptor = ArgumentCaptor.forClass(Following.class);

        //When
        followingService.follow(followingUser, user);

        //Then
        verify(followingRepository).save(argumentCaptor.capture());
        //And
        final Following following = argumentCaptor.getValue();

        assertThat(following.getUserId()).isEqualTo(user.getId());
        assertThat(following.getFollowingUserId()).isEqualTo(followingUser.getId());
    }

    @Test
    public void shouldNotDoAnythingIfUserAlreadyIsFollowingAUser() throws Exception {
        //Given
        final User followingUser = UserBuilder.userBuilder().build();
        final User user = UserBuilder.userBuilder().build();

        final Following expectedFollowing = followingBuilder()
                .userId(user.getId())
                .followingUserId(followingUser.getId())
                .build();
        given(followingRepository.findEntry(user.getId(), followingUser.getId()))
                .willReturn(Optional.of(expectedFollowing));
        //When
        followingService.follow(followingUser, user);

        //Then
        verify(followingRepository, never()).save(any(Following.class));
    }

    @Test
    public void shouldGetTimelineByUserId() {
        // Given
        final Long userId = longVal().next();

        //And
        final List<Long> expectedFollowingUserIds = list(longVal()).next();
        given(followingRepository.findAllFollowings(userId))
                .willReturn(expectedFollowingUserIds);

        //And
        final List<Tweet> expectedFollowingTweets = list(() -> new Tweet(longVal().next(),
                string().next(),
                userId,
                UUID.randomUUID(),
                LocalDateTime.now())).next();
        given(tweetRepository.findAllFollowingTweets(expectedFollowingUserIds))
                .willReturn(expectedFollowingTweets);

        // When
        final List<Tweet> timeline = followingService.getTimeline(userId);

        // Then
        assertThat(timeline).isNotEmpty();
    }

}