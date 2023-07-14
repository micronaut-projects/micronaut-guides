package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class BookControllerTest {

    @Inject
    @Client("/")
    StreamingHttpClient client;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    void testRetrieveBooks() {
        List<BookRecommendation> books = Flux.from(client.jsonStream(HttpRequest.GET("/books"), BookRecommendation.class)).collectList().block();
        assertEquals(books.size(), 1);
        assertEquals(books.get(0).name(), "Building Microservices");
    }
}
