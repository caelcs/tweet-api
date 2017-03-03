package uk.co.caeldev.tweet.api.features.following;

import uk.co.caeldev.tweet.api.features.tweets.Tweet;
import uk.co.caeldev.tweet.api.features.users.User;

import java.util.List;

public interface FollowingService {
    boolean follow(User followingUser, User user);

    List<Tweet> getTimeline(Long userId);
}
