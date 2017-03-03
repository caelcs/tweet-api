package uk.co.caeldev.tweet.api.config;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.caeldev.tweet.api.features.following.FollowingRepository;
import uk.co.caeldev.tweet.api.features.following.FollowingService;
import uk.co.caeldev.tweet.api.features.following.FollowingServiceImpl;
import uk.co.caeldev.tweet.api.features.tweets.TweetRepository;
import uk.co.caeldev.tweet.api.features.tweets.TweetResourceSupport;
import uk.co.caeldev.tweet.api.features.tweets.TweetService;
import uk.co.caeldev.tweet.api.features.tweets.TweetServiceImpl;
import uk.co.caeldev.tweet.api.features.users.UserRepository;
import uk.co.caeldev.tweet.api.features.users.UserService;
import uk.co.caeldev.tweet.api.features.users.UserServiceImpl;

@Configuration
public class BeanConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public TweetRepository tweetRepository() {
        return new TweetRepository();
    }

    @Bean
    public FollowingRepository followingRepository() {
        return new FollowingRepository();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }

    @Bean
    public FollowingService followingService(final FollowingRepository followingRepository,
                                             final TweetRepository tweetRepository) {
        return new FollowingServiceImpl(followingRepository, tweetRepository);
    }

    @Bean
    public TweetService tweetService(final TweetRepository tweetRepository) {
        return new TweetServiceImpl(tweetRepository);
    }

    @Bean
    public UserService userService(final UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public TweetResourceSupport tweetResourceSupport(final UserRepository userRepository) {
        return new TweetResourceSupport(userRepository);
    }
}
