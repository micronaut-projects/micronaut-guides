package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
class FruitValidationControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <2>

    @Test
    void fruitIsValidated() {
        HttpClientResponseException exception = assertThrows(
                HttpClientResponseException.class,
                () -> httpClient.toBlocking().exchange(HttpRequest.POST("/fruits", new FruitCommand("", "")))
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}
