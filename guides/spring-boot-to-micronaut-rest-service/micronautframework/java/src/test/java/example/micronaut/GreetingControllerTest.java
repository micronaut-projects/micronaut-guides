package example.micronaut;

import org.junit.jupiter.api.Test;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.HttpResponse;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class GreetingControllerTest {
    @Test
    void testGetStudentById_success_returnStudent(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String url = "/greeting";
        HttpResponse<String> response = client.exchange(url, String.class);
        assertEquals(HttpStatus.OK, response.status());
        Optional<String> jsonOptional = response.getBody();
        assertTrue(jsonOptional.isPresent());
        String json = jsonOptional.get();
        assertNotNull(json);
        assertTrue(json.startsWith("{\"id\""));
        assertTrue(json.endsWith(",\"content\":\"Hello, World!\"}"));
    }
}