package example.micronaut.controller;

import example.micronaut.model.WordFrequency;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@MicronautTest
@EnabledIfEnvironmentVariable(named = "TWITTER_AUTH_CLIENT_ID", matches = ".+")
public class TwitterFrequentWordsControllerTest {
    @Inject
    @Client("/twitter-frequent-words")
    HttpClient client;

    @Test
    public void testGet() {
        // GIVEN
        String searchQuery = "Europe";
        int wordsNumber = 20;
        URI uri = UriBuilder.of("/")
                .queryParam("search-query", searchQuery)
                .queryParam("words-n", wordsNumber).build();

        // WHEN
        HttpResponse<List<WordFrequency>> response = client.toBlocking()
                .exchange(HttpRequest.GET(uri), Argument.listOf(WordFrequency.class));

        // THEN
        assertEquals(HttpStatus.OK, response.status());
        List<WordFrequency> wordFrequencies = response.body();
        assertNotNull(wordFrequencies);
        assertEquals(wordsNumber, wordFrequencies.size());
        assertTrue(wordFrequencies.get(0).getWord().length() > 3);
        assertTrue(wordFrequencies.get(0).getNumberOccurred() > 2);
    }
}
