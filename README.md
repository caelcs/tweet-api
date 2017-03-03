# Tweet Api

## Overview

Rest Application that provides access to endpoints to manage all the tweets.
 
## Requierments
- Java 1.8

## Running

In order to run the app just execute:

./gradlew bootrun

To build a jar and run all the tests (Unit and e2e):

./gradlew test assemble

the application run on http://localhost:8080.

## Api Documentation

Once you run the app you will be able to see all the three apis
- tweets
- following
- timeline

http://localhost:8080/swagger-ui.html

### Notes

* In order to create a user it will be done by posting a meesage without setting in the header userGUID and the endpoint will return the tweet with the new userGUID.
* To post more messages to the same user, use the previous userGUID and set it as a header entry.
* The userGUID behaves in a similar way as a oauth2 token.




