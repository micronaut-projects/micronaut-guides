package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest // <1>
class PlanControllerRequiresAuthenticationTest {

    @Test
    void planControllerRequiresAuthentication(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        var ex = assertThrows(HttpClientResponseException.class,
                () -> client.exchange(HttpRequest.GET("/plan").accept(MediaType.TEXT_PLAIN)));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
    }
}