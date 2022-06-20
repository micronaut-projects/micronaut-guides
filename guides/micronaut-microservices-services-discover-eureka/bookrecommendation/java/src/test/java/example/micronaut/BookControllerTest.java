package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class BookControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    public void testRetrieveBooks() {
        List<BookRecommendation> books = client.toBlocking()
                .retrieve(HttpRequest.GET("/books"), Argument.listOf(BookRecommendation.class));
        assertEquals(1, books.size());
        assertEquals("Building Microservices", books.get(0).getName());
    }
}
