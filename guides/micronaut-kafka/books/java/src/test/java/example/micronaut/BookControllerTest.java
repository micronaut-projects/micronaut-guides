package example.micronaut;

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class BookControllerTest {

    private int clientInvocationCount;

    @Inject
    AnalyticsClient analyticsClient;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testMessageIsPublishedToKafkaWhenBookFound() {
        Optional<Book> result = retrieveGet("/books/1491950358");

        assertNotNull(result);
        assertTrue(result.isPresent());

        assertEquals(1, clientInvocationCount);
    }

    @Test
    void testMessageIsNotPublishedToKafkaWhenBookNotFound() {
        assertThrows(HttpClientResponseException.class, () -> {
            retrieveGet("/books/INVALID");
        });

        assertEquals(0, clientInvocationCount);
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return book -> clientInvocationCount++;
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
    }
}
