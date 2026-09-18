package example.micronaut

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetSpec {

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
        def response = restTestClient.get()
                .uri("/subscriptions/99")
                .exchange()

        response.expectStatus().isOk()
        def body = response.expectBody()
        body.jsonPath('$.id').isEqualTo(99)
        body.jsonPath('$.name').isEqualTo("Advanced")
        body.jsonPath('$.cents').isEqualTo(2900)
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        def response = restTestClient.get()
                .uri("/subscriptions/1000")
                .exchange()

        response.expectStatus().isNotFound()
        response.expectBody().isEmpty()
    }
}
