package example.micronaut;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionGetListControllerTest {

    @Autowired // <2>
    TestRestTemplate restTemplate; // <3>

    @Test
    void shouldReturnASortedPageOfSaasSubscriptions() {
        ResponseEntity<String> response = restTemplate.getForEntity("/subscriptions?page=0&size=1&sort=cents,desc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        Integer cents = documentContext.read("$[0].cents");
        assertThat(cents).isEqualTo(4900);

        response = restTemplate.getForEntity("/subscriptions?page=0&size=1", String.class);
        documentContext = JsonPath.parse(response.getBody());
        cents = documentContext.read("$[0].cents");
        assertThat(cents).isEqualTo(1400);
    }

    @Test
    void shouldReturnAPageOfSaasSubscriptions() {
        ResponseEntity<String> response = restTemplate.getForEntity("/subscriptions?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.getForEntity("/subscriptions", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int saasSubscriptionCount = documentContext.read("$.length()");
        assertThat(saasSubscriptionCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray cents = documentContext.read("$..cents");
        assertThat(cents).containsExactlyInAnyOrder(1400, 2900, 4900);
    }

}