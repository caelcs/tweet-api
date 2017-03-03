package uk.co.caeldev.tweet.api.features.following;

import static uk.org.fyodor.generators.RDG.longVal;

public class FollowingBuilder {

    private Long followingUserId;
    private Long userId;

    FollowingBuilder() {
    }

    public static FollowingBuilder followingBuilder() {
        return new FollowingBuilder();
    }

    public Following build() {
        return new Following(userId, followingUserId);
    }

    public FollowingBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public FollowingBuilder followingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
        return this;
    }
}
