// tag::imports[]
package example.micronaut.controller;

import example.twitter.api.TweetsApi;
import example.twitter.model.TweetSearchResponse;
import example.micronaut.model.WordFrequency;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// end::imports[]

// tag::class-begin[]
@Controller("/twitter-frequent-words") // <1>
public class TwitterFrequentWordsController {
// end::class-begin[]

    // tag::api[]
    public static int TWEETS_NUMBER = 50;

    private final TweetsApi tweetsApi;

    public TwitterFrequentWordsController(TweetsApi tweetsApi) { // <2>
        this.tweetsApi = tweetsApi;
    }

    @Get // <3>
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Flux<WordFrequency> get(@QueryValue("search-query") String searchQuery,
                                   @QueryValue("words-n") Integer wordsNumber // <4>
    ) {
        return tweetsApi.tweetsRecentSearch(searchQuery,
                null, null, null, null, TWEETS_NUMBER, null, null,
                null, null, null, null, null)
                .flatMapIterable(v -> getTopFrequentWords(v, wordsNumber)); // <5>
    }
    // end::api[]

    // tag::method[]
    @NonNull
    private static Map<String, Integer> getWordOccurrences(@NonNull TweetSearchResponse response) { // <1>
        Map<String, Integer> wordOccurrences = new HashMap<>(); // <2>

        response.getData().forEach(tweet ->
                Arrays.stream(tweet.getText().split("[^a-zA-Z]+"))
                        .map(String::toLowerCase)
                        .filter(w -> w.length() > 3)
                        .forEach(word -> wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1)) // <3>
        );

        return wordOccurrences;
    }

    @NonNull
    private static List<WordFrequency> getTopFrequentWords(@NonNull TweetSearchResponse response,
                                                           @NonNull Integer wordsNumber) {
        if (response.getData() == null) {
            return Collections.emptyList();
        }

        return getWordOccurrences(response).entrySet().stream()
                .sorted(Map.Entry.comparingByValue(TwitterFrequentWordsController::compareIntegersReversed))
                .limit(wordsNumber)
                .map(v -> new WordFrequency(v.getKey(), v.getValue()))
                .collect(Collectors.toList()); // <4>
    }

    private static Integer compareIntegersReversed(Integer v1, Integer v2) {
        return v2 - v1;
    }
    // end::method[]
// tag::class-end[]
}
// end::class-end[]
