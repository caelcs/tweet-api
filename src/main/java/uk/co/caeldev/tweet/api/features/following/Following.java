package uk.co.caeldev.tweet.api.features.following;

public class Following {

    private Long userId;
    private Long followingUserId;

    public Following(Long userId, Long followingUserId) {
        this.userId = userId;
        this.followingUserId = followingUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getFollowingUserId() {
        return followingUserId;
    }
}
