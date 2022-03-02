// tag::imports[]
package example.twitter.api;

import example.twitter.model.SearchCount;
import example.twitter.model.Tweet;
import example.twitter.model.TweetCountsResponse;
import example.twitter.model.TweetSearchResponse;
import example.twitter.model.User;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
// end::imports[]

//tag::class-begin[]
/**
 * API tests for TweetsApi
 */
@MicronautTest // <1>
public class TweetsApiTest {

    @Inject
    TweetsApi api; // <2>

    // end::class-begin[]

    // tag::test-1[]
    /**
     * Recent search counts
     */
    @Test
    public void tweetCountsRecentSearchTest() {
        // WHEN
        String query = "Toronto";
        TweetCountsResponse response = api.tweetCountsRecentSearch(
                query, null, null, null, null, null, null).block(); // <3>

        // THEN
        assertNotNull(response);
        // Calculate total count
        assertNotNull(response.getData());
        Integer totalCount = response.getData().stream()
                .filter(Objects::nonNull)
                .map(SearchCount::getTweetCount)
                .reduce(0, Integer::sum);
        // There should be more than 100 tweets with such query in the last 7 days
        assertTrue(totalCount > 100); // <4>
    }
    // end::test-1[]

    // tag::test-2[]
    /**
     * Recent search
     */
    @Test
    public void tweetsRecentSearchTest() {
        // GIVEN
        String query = "nyc"; // <1>
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        String sinceId = null;
        String untilId = null;
        Integer maxResults = 10;
        String nextToken = null;
        Set<String> expansions = new HashSet<>(Collections.singletonList("author_id")); // <2>
        Set<String> tweetFields = new HashSet<>(Arrays.asList( // <3>
                "id", "author_id", "created_at", "attachments", "lang", "possibly_sensitive", "text", "source"
        ));
        Set<String> userFields = new HashSet<>(Arrays.asList("description", "created_at", "username", "name")); // <4>
        Set<String> mediaFields = null;
        Set<String> placeFields = null;
        Set<String> pollFields = null;

        // WHEN
        TweetSearchResponse response = api.tweetsRecentSearch(
                query, startTime, endTime, sinceId, untilId, maxResults, nextToken, expansions, tweetFields,
                userFields, mediaFields, placeFields, pollFields).block(); // <5>

        // THEN
        assertNotNull(response);

        // Tweets should be present
        List<Tweet> tweets = response.getData(); // <6>
        assertNotNull(tweets);
        assertEquals(maxResults, tweets.size());

        Tweet tweet = tweets.get(0);
        assertNotNull(tweet);
        assertNotNull(tweet.getAuthorId());
        assertNotNull(tweet.getCreatedAt());
        assertNotNull(tweet.getLang());
        assertNotNull(tweet.getPossiblySensitive());
        assertNotNull(tweet.getText());

        // Users should be present
        assertNotNull(response.getIncludes());
        List<User> users = response.getIncludes().getUsers(); // <7>
        assertNotNull(users);
        assertTrue(users.size() > 0);

        User user = users.get(0);
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(user.getUsername());
    }
    // end::test-2[]

    // tag::test-3[]
    /**
     * Recent search with the nextToken parameter set (to retrieve batch)
     */
    @Test
    public void tweetsRecentSearchTestNextToken() {
        // WHEN
        String query = "Toronto";
        int maxResults = 10;
        TweetSearchResponse response = api.tweetsRecentSearch(query, null, null, null,
                null, maxResults, null, null, null, null, null, null, null).block(); // <1>

        // THEN
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(maxResults, response.getData().size());
        Set<String> tweetIds = response.getData().stream().map(Tweet::getId).collect(Collectors.toSet());

        assertNotNull(response.getMeta());
        String nextToken = response.getMeta().getNextToken();
        assertNotNull(nextToken);

        // WHEN
        TweetSearchResponse nextResponse = api.tweetsRecentSearch(query, null, null, null,
                null, maxResults, nextToken, null, null, null, null,
                null, null).block(); // <2>

        // THEN
        assertNotNull(nextResponse);
        assertNotNull(nextResponse.getData());
        assertEquals(maxResults, nextResponse.getData().size());
        // Calculate the intersection and verify that no match
        Set<String> nextTweetIds = nextResponse.getData().stream().map(Tweet::getId).collect(Collectors.toSet());
        nextTweetIds.retainAll(tweetIds);
        assertTrue(nextTweetIds.isEmpty()); // <3>
    }
    // end::test-3[]
// tag::class-end[]
}
// end::class-end[]
