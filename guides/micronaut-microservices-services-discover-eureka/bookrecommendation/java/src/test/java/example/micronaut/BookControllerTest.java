package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.StreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import reactor.core.publisher.Flux;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import jakarta.inject.Inject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

@MicronautTest
public class BookControllerTest {

    @Inject
    @Client("/")
    StreamingHttpClient client;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    public void testRetrieveBooks() {
        List<BookRecommendation> books = Flux.from(client.jsonStream(HttpRequest.GET("/books"), BookRecommendation.class)).collectList().block();
        assertEquals(books.size(), 1);
        assertEquals(books.get(0).getName(), "Building Microservices");
    }
}
