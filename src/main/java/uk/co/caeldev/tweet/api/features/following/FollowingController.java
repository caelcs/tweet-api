package uk.co.caeldev.tweet.api.features.following;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.tweet.api.features.tweets.Tweet;
import uk.co.caeldev.tweet.api.features.tweets.TweetResource;
import uk.co.caeldev.tweet.api.features.tweets.TweetResourceSupport;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FollowingController {

    private final FollowingService followingService;
    private final UserService userService;
    private final TweetResourceSupport tweetResourceSupport;

    @Autowired
    public FollowingController(final FollowingService followingService,
                               final UserService userService,
                               final TweetResourceSupport tweetResourceSupport) {
        this.followingService = followingService;
        this.userService = userService;
        this.tweetResourceSupport = tweetResourceSupport;
    }

    @RequestMapping(value = "/following/{followingUserUUID}",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Following> follow(@PathVariable UUID followingUserUUID,
                                            @RequestHeader UUID userGUID) {
        Optional<User> followingUser = userService.findByGUID(followingUserUUID);
        if (!followingUser.isPresent()) {
            throw new IllegalArgumentException("following user GUID does not exist");
        }

        Optional<User> user = userService.findByGUID(userGUID);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("user GUID does not exist");
        }

        followingService.follow(followingUser.get(), user.get());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/timeline",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<TweetResource>> timeline(@RequestHeader UUID userGUID) {
        final Optional<User> user = userService.findByGUID(userGUID);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User GUID invalid");
        }

        final List<Tweet> timeline = followingService.getTimeline(user.get().getId());
        final List<TweetResource> timelineResource = timeline.stream()
                .map(tweet -> tweetResourceSupport.toResource(tweet))
                .collect(Collectors.toList());
        return new ResponseEntity<>(timelineResource, HttpStatus.OK);
    }
}
