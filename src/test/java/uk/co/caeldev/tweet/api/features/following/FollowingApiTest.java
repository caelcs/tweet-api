package uk.co.caeldev.tweet.api.features.following;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.tweet.api.BaseIntegrationTest;
import uk.co.caeldev.tweet.api.features.tweets.TweetResource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpStatus.*;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringRunner.class)
public class FollowingApiTest extends BaseIntegrationTest {

    @Test
    public void shouldFollowAUser() {
        //Given
        TweetResource tweetResourceUser1 = given()
                .port(serverPort)
                .body(string().next())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        TweetResource tweetResourceUser2 = given()
                .port(serverPort)
                .body(string().next())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        given()
            .port(serverPort)
            .header("userGUID", tweetResourceUser2.getUserGUID())
        .when()
            .post("/following/" + tweetResourceUser1.getUserGUID())
        .then()
            .assertThat()
            .statusCode(equalTo(CREATED.value()));
    }

    @Test
    public void shouldNotFollowWhenUserGUIDIsInvalid() {
        //Given
        TweetResource tweetResourceUser1 = given()
                .port(serverPort)
                .body(string().next())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        given()
                .port(serverPort)
                .header("userGUID", tweetResourceUser1.getUserGUID())
                .when()
                .post("/following/" + UUID.randomUUID())
                .then()
                .assertThat()
                .statusCode(equalTo(BAD_REQUEST.value()));
    }

    @Test
    public void shouldGetAllTweetsFromUsersThatFollows() {
        //Given
        TweetResource tweetResourceUser1 = given()
                .port(serverPort)
                .body(string().next())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        TweetResource tweetResourceUser2 = given()
                .port(serverPort)
                .body(string().next())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        TweetResource tweetResource2User1 = given()
                .port(serverPort)
                .body(string().next())
                .header("userGUID", tweetResourceUser1.getUserGUID())
                .when()
                .post("/tweets/")
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value())).extract().body().as(TweetResource.class);

        given()
                .port(serverPort)
                .header("userGUID", tweetResourceUser2.getUserGUID())
                .when()
                .post("/following/" + tweetResourceUser1.getUserGUID())
                .then()
                .assertThat()
                .statusCode(equalTo(CREATED.value()));

        List<LinkedHashMap> timelineUser2 = given()
                .port(serverPort)
                .header("userGUID", tweetResourceUser2.getUserGUID())
                .when()
                .get("/timeline")
                .then()
                .assertThat()
                .statusCode(equalTo(OK.value())).extract().body().as(List.class);

        List<LinkedHashMap> timelineUser1 = given()
                .port(serverPort)
                .header("userGUID", tweetResourceUser1.getUserGUID())
                .when()
                .get("/timeline")
                .then()
                .assertThat()
                .statusCode(equalTo(OK.value())).extract().body().as(List.class);

        assertThat(timelineUser2).hasSize(2);
        assertThat(timelineUser2.get(0).get("tweetGUID")).isEqualTo(tweetResource2User1.getTweetGUID().toString());
        assertThat(timelineUser2.get(1).get("tweetGUID")).isEqualTo(tweetResourceUser1.getTweetGUID().toString());

        assertThat(timelineUser1).isEmpty();
    }


}
