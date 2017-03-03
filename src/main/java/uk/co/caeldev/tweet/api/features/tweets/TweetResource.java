package uk.co.caeldev.tweet.api.features.tweets;

import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

public class TweetResource extends ResourceSupport {

    private UUID tweetGUID;
    private UUID userGUID;
    private String message;

    public TweetResource() {
    }

    public TweetResource(UUID tweetGUID, UUID userGUID, String message) {
        this.tweetGUID = tweetGUID;
        this.userGUID = userGUID;
        this.message = message;
    }

    public UUID getTweetGUID() {
        return tweetGUID;
    }

    public UUID getUserGUID() {
        return userGUID;
    }

    public String getMessage() {
        return message;
    }
}
