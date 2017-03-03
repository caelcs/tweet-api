package uk.co.caeldev.tweet.api.features.following;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FollowingRepository {

    private Set<Following> followings;

    public FollowingRepository() {
        this.followings = new HashSet<>();
    }

    public void save(Following following) {
        followings.add(following);
    }

    public Optional<Following> findEntry(Long userId, Long followingId) {
        return followings.stream()
                .filter(following -> following.getUserId().equals(userId)
                        && following.getFollowingUserId().equals(followingId))
                .findFirst();
    }

    public List<Long> findAllFollowings(Long userId) {
        return followings.stream()
                .filter((following) -> following.getUserId().equals(userId))
                .map((following -> following.getFollowingUserId()))
                .collect(Collectors.toList());
    }
}
