package example.micronaut;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetTest {

    @Autowired // <2>
    TestRestTemplate restTemplate; // <3>

    @Test
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/subscriptions/99", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(99);

        String name = documentContext.read("$.name");
        assertThat(name).isNotNull();
        assertThat(name).isEqualTo("Advanced");

        Integer cents = documentContext.read("$.cents");
        assertThat(cents).isEqualTo(2900);
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/subscriptions/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }
}
