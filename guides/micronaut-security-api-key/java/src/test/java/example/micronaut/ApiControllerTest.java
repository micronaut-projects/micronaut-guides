package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "api-keys.companyA.key", value = "XXX") // <1>
@Property(name = "api-keys.companyA.name", value = "John") // <1>
@Property(name = "api-keys.companyB.key", value = "YYY") // <1>
@Property(name = "api-keys.companyB.name", value = "Paul") // <1>
@MicronautTest // <2>
class ApiControllerTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Test
    void apiIsSecured() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/api").accept(MediaType.TEXT_PLAIN);
        Executable e = () -> client.exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
    }

    @Test
    void apiNotAccessibleIfWrongKey() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = createRequest("ZZZ");
        Executable e = () -> client.exchange(request);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getStatus());
    }

    @Test
    void apiIsAccessibleWithAnApiKey() {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<String> response = assertDoesNotThrow(() -> client.exchange(createRequest("XXX"), String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        Optional<String> body = response.getBody();
        assertTrue(body.isPresent());
        assertEquals("Hello John", body.get());

        response = assertDoesNotThrow(() -> client.exchange(createRequest("YYY"), String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        body = response.getBody();
        assertTrue(body.isPresent());
        assertEquals("Hello Paul", body.get());
    }

    private static HttpRequest<?> createRequest(String apiKey) {
        return HttpRequest.GET("/api")
                .accept(MediaType.TEXT_PLAIN)
                .header("X-API-KEY", apiKey);
    }
}