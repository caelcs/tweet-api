package uk.co.caeldev.tweet.api.features.users;

import java.util.UUID;

public class User {

    private final Long id;
    private final UUID userGUID;

    public User(Long id, UUID userGUID) {
        this.id = id;
        this.userGUID = userGUID;
    }

    public Long getId() {
        return id;
    }

    public UUID getUserGUID() {
        return userGUID;
    }
}
