package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxStreamingHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.reactivex.Flowable;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class BookControllerTest {

    @Inject
    @Client("/")
    RxStreamingHttpClient client;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    public void testRetrieveBooks() {
        Flowable<BookRecommendation> books = client.jsonStream(HttpRequest.GET("/books"), BookRecommendation.class);
        assertEquals(books.toList().blockingGet().size(), 1);
        assertEquals(books.toList().blockingGet().get(0).getName(), "Building Microservices");
    }
}
