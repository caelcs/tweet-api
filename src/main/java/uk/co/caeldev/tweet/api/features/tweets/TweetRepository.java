package uk.co.caeldev.tweet.api.features.tweets;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TweetRepository {

    private List<Tweet> tweets;
    private ZoneId currentZone;

    public TweetRepository() {
        this.tweets = new ArrayList<>();
        this.currentZone = ZoneId.systemDefault();
    }

    public Tweet save(Tweet tweet) {
        final LocalDateTime now = LocalDateTime.now();
        final ZonedDateTime zdt = now.atZone(currentZone);

        final Tweet tweetToBeSaved = new Tweet(zdt.toInstant().toEpochMilli(), tweet.getMessage(), tweet.getUserId(), tweet.getTweetGUID(), now);
        tweets.add(tweetToBeSaved);
        return tweetToBeSaved;
    }

    public List<Tweet> getAllByUserId(Long userId) {
        return tweets.stream()
                .filter(tweet -> tweet.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Tweet> findAllFollowingTweets(List<Long> expectedFollowingUserIds) {
        return tweets.stream()
                .filter((tweet -> expectedFollowingUserIds.contains(tweet.getUserId())))
                .sorted(Comparator.comparing(Tweet::getCreationDate).reversed())
                .collect(Collectors.toList());
    }
}
