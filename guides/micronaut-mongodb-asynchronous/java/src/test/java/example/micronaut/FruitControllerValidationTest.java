package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "FruitControllerValidationTest")
@MicronautTest
public class FruitControllerValidationTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void testFruitIsValidated() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
                httpClient.toBlocking().exchange(HttpRequest.POST("/fruits", new Fruit("", "Hola"))));
        assertEquals(BAD_REQUEST, e.getStatus());
    }
}
