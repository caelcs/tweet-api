package uk.co.caeldev.tweet.api.features.users;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    private List<User> users;
    private ZoneId currentZone;

    public UserRepository() {
        users = new ArrayList<>();
        this.currentZone = ZoneId.systemDefault();
    }

    public Optional<User> findUserById(Long userId) {
        return users.stream()
                .filter((User user) -> user.getId().equals(userId))
                .findFirst();
    }

    public Optional<User> findUserByUUID(UUID userGUID) {
        return users.stream()
                .filter((User user) -> user.getUserGUID().equals(userGUID))
                .findFirst();
    }

    public User create() {
        final LocalDateTime now = LocalDateTime.now();
        final ZonedDateTime zdt = now.atZone(currentZone);

        final User user = new User(zdt.toInstant().toEpochMilli(), UUID.randomUUID());
        users.add(user);
        return user;
    }
}
