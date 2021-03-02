package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class BooksControllerTest {

    @Inject
    @Client("/")
    RxHttpClient rxHttpClient;

    @Test
    public void testBooksController() {
        HttpResponse<Boolean> rsp = rxHttpClient.toBlocking().exchange(HttpRequest.GET("/books/stock/1491950358"), Boolean.class);
        assertEquals(rsp.status(), HttpStatus.OK);
        assertTrue(rsp.body());
    }

    @Test
    public void testBooksControllerWithNonExistingIsbn() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            rxHttpClient.toBlocking().exchange(HttpRequest.GET("/books/stock/XXXXX"), Boolean.class);
        });

        assertEquals(
                HttpStatus.NOT_FOUND,
                thrown.getResponse().getStatus()
        );

    }
}
