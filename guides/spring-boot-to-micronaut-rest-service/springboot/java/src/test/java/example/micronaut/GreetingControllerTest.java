package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GreetingControllerTest {
    @Autowired
    private TestRestTemplate client;

    @Test
    void testGetStudentById_success_returnStudent() {
        String url = "/greeting";
        ResponseEntity<String> response = client.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String json = response.getBody();
        assertNotNull(json);
        assertTrue(json.startsWith("{\"id\""));
        assertTrue(json.endsWith(",\"content\":\"Hello, World!\"}"));
    }
}