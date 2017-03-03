package uk.co.caeldev.tweet.api.features.following;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.tweet.api.features.tweets.Tweet;
import uk.co.caeldev.tweet.api.features.tweets.TweetRepository;
import uk.co.caeldev.tweet.api.features.users.User;

import java.util.List;
import java.util.Optional;

public class FollowingServiceImpl implements FollowingService {

    private final FollowingRepository followingRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public FollowingServiceImpl(final FollowingRepository followingRepository,
                                final TweetRepository tweetRepository) {
        this.followingRepository = followingRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public boolean follow(User followingUser, User user) {
        final Optional<Following> followingEntry = followingRepository.findEntry(user.getId(), followingUser.getId());

        if (followingEntry.isPresent()) {
            return true;
        }

        final Following following = new Following(user.getId(), followingUser.getId());
        followingRepository.save(following);
        return true;
    }

    @Override
    public List<Tweet> getTimeline(Long userId) {
        final List<Long> allFollowings = followingRepository.findAllFollowings(userId);
        return tweetRepository.findAllFollowingTweets(allFollowings);
    }
}
