package uk.co.caeldev.tweet.api.features.users;

import java.util.UUID;

import static uk.org.fyodor.generators.RDG.longVal;

public class UserBuilder {

    private UUID userGUID = UUID.randomUUID();
    private Long id = longVal().next();

    UserBuilder() {
    }

    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }

    public User build() {
        return new User(id, userGUID);
    }

    public UserBuilder userGUID(UUID userGUID) {
        this.userGUID = userGUID;
        return this;
    }
}
