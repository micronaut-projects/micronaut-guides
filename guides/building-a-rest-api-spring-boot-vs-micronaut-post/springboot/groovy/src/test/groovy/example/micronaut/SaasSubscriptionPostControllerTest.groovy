package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.EntityExchangeResult
import org.springframework.test.web.servlet.client.RestTestClient

import static org.assertj.core.api.Assertions.assertThat

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionPostControllerTest {

    @LocalServerPort // <2>
    int port

    RestTestClient restTestClient // <3>

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    void shouldCreateANewSaasSubscription() {
        SaasSubscription newSaasSubscription = new SaasSubscription(name: 'Advanced', cents: 2500)
        EntityExchangeResult<Void> createResponse = restTestClient.post()
                .uri('/subscriptions')
                .body(newSaasSubscription)
                .exchange()
                .expectBody()
                .isEmpty()
        assertThat(createResponse.status).isEqualTo(HttpStatus.CREATED)

        URI locationOfNewSaasSubscription = createResponse.responseHeaders.location
        EntityExchangeResult<String> getResponse = restTestClient.get()
                .uri(locationOfNewSaasSubscription)
                .exchange()
                .returnResult(String)
        assertThat(getResponse.status).isEqualTo(HttpStatus.OK)

        DocumentContext documentContext = JsonPath.parse(getResponse.responseBody)
        Number id = documentContext.read('$.id')
        assertThat(id).isNotNull()

        Integer cents = documentContext.read('$.cents')
        assertThat(cents).isEqualTo(2500)
    }
}
