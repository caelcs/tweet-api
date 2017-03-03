package uk.co.caeldev.tweet.api.features.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.tweet.api.features.users.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetServiceImpl(final TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Tweet postTweet(final String message,
                           final User user) {
        final UUID tweetGUID = UUID.randomUUID();

        if (message.length() > 140) {
            throw new IllegalArgumentException("Message exceed 140 characters limit");
        }

        return tweetRepository.save(new Tweet(user.getId(), tweetGUID, message));
    }

    @Override
    public List<Tweet> getTweetsByUserId(Long userId) {
        final List<Tweet> allByUserId = tweetRepository.getAllByUserId(userId);
        return allByUserId
                .stream()
                .sorted(Comparator.comparing(Tweet::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Tweet> getTimeline(Long userId) {
        return null;
    }
}
