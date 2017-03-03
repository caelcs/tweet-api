package uk.co.caeldev.tweet.api.features.tweets;

import uk.co.caeldev.tweet.api.features.users.User;

import java.util.List;

public interface TweetService {
    Tweet postTweet(String message, User user);

    List<Tweet> getTweetsByUserId(Long id);

    List<Tweet> getTimeline(Long userId);
}
