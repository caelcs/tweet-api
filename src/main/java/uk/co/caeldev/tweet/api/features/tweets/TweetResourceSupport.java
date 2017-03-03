package uk.co.caeldev.tweet.api.features.tweets;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserRepository;

import java.util.Optional;

public class TweetResourceSupport extends ResourceAssemblerSupport<Tweet, TweetResource> {

    private final UserRepository userRepository;

    public TweetResourceSupport(final UserRepository userRepository) {
        super(TweetController.class, TweetResource.class);
        this.userRepository = userRepository;
    }

    @Override
    public TweetResource toResource(final Tweet tweet) {
        final Optional<User> user = userRepository.findUserById(tweet.getUserId());
        return new TweetResource(tweet.getTweetGUID(), user.get().getUserGUID(), tweet.getMessage());
    }
}
