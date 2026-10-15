package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthTest {
    @Autowired
    private TestRestTemplate client;

    @Test
    void healthEndpointReturnsOkAndStatusUp() {
        String url = "/actuator/health";
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String json = response.getBody();
        assertNotNull(json);
        assertEquals("{\"status\":\"UP\"}", json);
    }
}
