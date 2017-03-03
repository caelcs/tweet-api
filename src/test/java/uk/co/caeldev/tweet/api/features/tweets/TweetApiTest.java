package uk.co.caeldev.tweet.api.features.tweets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.tweet.api.BaseIntegrationTest;
import uk.co.caeldev.tweet.api.features.users.User;
import uk.co.caeldev.tweet.api.features.users.UserRepository;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class TweetApiTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldPostANewTweetAndCreateAUser() {
        String message = "this is a test";
        final TweetResource responseBody = given()
                .port(serverPort)
                .body(message)
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        assertThat(responseBody.getUserGUID()).isNotNull();
        assertThat(responseBody.getMessage()).isEqualTo(message);
    }

    @Test
    public void shouldPostANewTweetBehalfOfExistingUser() {
        User user = userRepository.create();
        String message = "this is a test";
        TweetResource responseTweet = given()
                .port(serverPort)
                .body(message)
                .header("userGUID", user.getUserGUID())
            .when()
                .post("/tweets/")
            .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        assertThat(responseTweet.getMessage()).isEqualTo(message);
        assertThat(responseTweet.getUserGUID()).isEqualTo(user.getUserGUID());
        assertThat(responseTweet.getTweetGUID()).isNotNull();
    }

    @Test
    public void shouldNotPostANewTweetAndGetBadRequest() {
        String message = "this is a test";
        given()
            .port(serverPort)
            .body(message)
            .header("userGUID", UUID.randomUUID())
        .when()
            .post("/tweets/")
        .then()
            .assertThat()
            .statusCode(equalTo(BAD_REQUEST.value()));
    }

    @Test
    public void shouldGetAllTweetsForAValidUserGUID() {
        String message = "this is a test";
        final TweetResource responseBody = given()
                .port(serverPort)
                .body(message)
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        given()
                .port(serverPort)
                .body(message)
                .header("userGUID", responseBody.getUserGUID())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value()));

        List<TweetResource> tweets = given()
                .port(serverPort)
                .header("userGUID", responseBody.getUserGUID())
                .when()
                .get("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(OK.value())).extract().body().as(List.class);

        assertThat(tweets).hasSize(2);
    }

}
