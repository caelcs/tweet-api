package uk.co.caeldev.tweet.api.features.tweets;

import java.time.LocalDateTime;
import java.util.UUID;

public class Tweet {

    private Long id;
    private final Long userId;
    private final UUID tweetGUID;
    private final String message;
    private LocalDateTime creationDate;

    public Tweet(final Long id,
                 final String message,
                 final Long userId,
                 final UUID tweetGUID,
                 final LocalDateTime creationDate) {
        this.id = id;
        this.message = message;
        this.userId = userId;
        this.tweetGUID = tweetGUID;
        this.creationDate = creationDate;
    }

    public Tweet(Long userId, UUID tweetGUID, String message) {
        this.userId = userId;
        this.tweetGUID = tweetGUID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public UUID getTweetGUID() {
        return tweetGUID;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}
