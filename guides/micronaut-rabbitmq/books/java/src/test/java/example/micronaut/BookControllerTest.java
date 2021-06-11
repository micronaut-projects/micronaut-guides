package example.micronaut;

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@MicronautTest
public class BookControllerTest {

    @Inject
    AnalyticsClient analyticsClient;

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testMessageIsPublishedToRabbitMQWhenBookFound() {
        Optional<Book> result = retrieveGet("/books/1491950358");

        assertNotNull(result);
        assertTrue(result.isPresent());

        verify(analyticsClient, times(1)).updateAnalytics(result.get());
    }

    @Test
    void testMessageIsNotPublishedToRabbitMQWhenBookNotFound() {
        assertThrows(HttpClientResponseException.class, () -> {
            Optional<Book> result = retrieveGet("/books/INVALID");

            assertNotNull(result);
            assertFalse(result.isPresent());
        });

        verifyZeroInteractions(analyticsClient);
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return Mockito.mock(AnalyticsClient.class);
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
    }

}
