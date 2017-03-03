package uk.co.caeldev.tweet.api.features.users;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findOrCreateByGUID(UUID userGUID) {
        if (Objects.isNull(userGUID)) {
            return userRepository.create();
        }

        Optional<User> userByUUID = userRepository.findUserByUUID(userGUID);

        if (!userByUUID.isPresent()) {
            throw new IllegalArgumentException("userGUID invalid");
        }

        return userByUUID.get();
    }

    @Override
    public Optional<User> findByGUID(UUID userGUID) {
        return userRepository.findUserByUUID(userGUID);
    }
}
