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
class SaasSubscriptionControllerGetTest {

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
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        EntityExchangeResult<String> response = restTestClient.get()
                .uri('/subscriptions/99')
                .exchange()
                .returnResult(String)

        assertThat(response.status).isEqualTo(HttpStatus.OK)

        DocumentContext documentContext = JsonPath.parse(response.responseBody)
        Number id = documentContext.read('$.id')
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(99)

        String name = documentContext.read('$.name')
        assertThat(name).isNotNull()
        assertThat(name).isEqualTo('Advanced')

        Integer cents = documentContext.read('$.cents')
        assertThat(cents).isEqualTo(2900)
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        EntityExchangeResult<String> response = restTestClient.get()
                .uri('/subscriptions/1000')
                .exchange()
                .returnResult(String)

        assertThat(response.status).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat((String) response.responseBody).isBlank()
    }
}
