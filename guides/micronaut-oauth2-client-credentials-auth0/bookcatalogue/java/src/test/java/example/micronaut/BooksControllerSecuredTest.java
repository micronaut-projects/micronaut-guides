package example.micronaut;

import io.micronaut.http.HttpRequest;
import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class BooksControllerSecuredTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void testBooksControllerIsSecured() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                httpClient.toBlocking().exchange(HttpRequest.GET("/books"), Boolean.class));
        assertEquals(UNAUTHORIZED, e.getStatus());
    }
}
