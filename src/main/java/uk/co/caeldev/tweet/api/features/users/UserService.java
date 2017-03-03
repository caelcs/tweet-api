package uk.co.caeldev.tweet.api.features.users;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User findOrCreateByGUID(UUID userGUID);

    Optional<User> findByGUID(UUID followingUserGUID);
}
