package uk.co.caeldev.tweet.api.features.tweets;

import uk.co.caeldev.tweet.api.features.users.User;
import uk.org.fyodor.generators.Generator;

import java.time.LocalDateTime;
import java.util.UUID;

import static uk.org.fyodor.generators.RDG.longVal;
import static uk.org.fyodor.generators.RDG.string;

public class TweetBuilder {

    private String message = string().next();
    private User user = user().next();
    private UUID tweetGUID = UUID.randomUUID();
    private Long id = longVal().next();
    private LocalDateTime creationDate = LocalDateTime.now();
    private LocalDateTime dateToBeSet;

    TweetBuilder() {
    }

    public static TweetBuilder tweetBuilder() {
        return new TweetBuilder();
    }

    public Tweet build() {
        return new Tweet(id, message, user.getId(), tweetGUID, creationDate);
    }

    public TweetBuilder message(String message) {
        this.message = message;
        return this;
    }

    public TweetBuilder user(User user) {
        this.user = user;
        return this;
    }

    public TweetBuilder creationDate(LocalDateTime dateToBeSet) {
        this.creationDate = dateToBeSet;
        return this;
    }

    public TweetBuilder noId() {
        this.id = null;
        return this;
    }

    private Generator<User> user() {
        return () -> new User(longVal().next(), UUID.randomUUID());
    }
}
