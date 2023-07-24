package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class HealthTest {
    @Test
    void healthEndpointReturnsOkAndStatusUp(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String url = "/actuator/health";
        HttpResponse<String> response = client.exchange(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        Optional<String> jsonOptional = response.getBody();
        assertTrue(jsonOptional.isPresent());
        String json = jsonOptional.get();
        assertNotNull(json);
        assertEquals("{\"status\":\"UP\"}", json);
    }
}
