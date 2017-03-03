package uk.co.caeldev.tweet.api.features.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;
    private final TweetResourceSupport tweetResourceSupport;

    @Autowired
    public TweetController(final TweetService tweetService,
                           final UserService userService,
                           final TweetResourceSupport tweetResourceSupport) {
        this.tweetService = tweetService;
        this.userService = userService;
        this.tweetResourceSupport = tweetResourceSupport;
    }

    @RequestMapping(value = "/tweets",
            method = RequestMethod.POST,
            consumes = {MediaType.TEXT_PLAIN_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<TweetResource> createTweet(@RequestBody String message,
                                             @RequestHeader(required = false) UUID userGUID) {

        final User user = userService.findOrCreateByGUID(userGUID);

        final Tweet tweet = tweetService.postTweet(message, user);

        return new ResponseEntity<>(tweetResourceSupport.toResource(tweet), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/tweets",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<TweetResource>> getTweets(@RequestHeader UUID userGUID) {

        final User user = userService.findOrCreateByGUID(userGUID);

        final List<Tweet> tweets = tweetService.getTweetsByUserId(user.getId());
        final List<TweetResource> results = tweets.stream().map(tweet -> tweetResourceSupport.toResource(tweet)).collect(Collectors.toList());

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
