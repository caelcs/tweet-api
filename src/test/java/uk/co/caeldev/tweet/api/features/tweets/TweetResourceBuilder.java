package uk.co.caeldev.tweet.api.features.tweets;


import java.util.UUID;

public class TweetResourceBuilder {
    private Tweet tweet;
    private UUID userGUID = UUID.randomUUID();

    TweetResourceBuilder() {
    }

    public static TweetResourceBuilder tweetResourceBuilder() {
        return new TweetResourceBuilder();
    }

    public TweetResourceBuilder tweet(Tweet tweet) {
        this.tweet = tweet;
        return this;
    }

    public TweetResourceBuilder userGUID(UUID userGUID) {
        this.userGUID = userGUID;
        return this;
    }

    public TweetResource build() {
        return new TweetResource(tweet.getTweetGUID(), userGUID, tweet.getMessage());
    }
}
