package example.micronaut

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetTest {

    @LocalServerPort // <2>
    var port = 0

    private lateinit var restTestClient: RestTestClient // <3>

    @BeforeEach
    fun setUp() {
        restTestClient = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun shouldReturnASaasSubscriptionWhenDataIsSaved() {
        val response = restTestClient.get()
            .uri("/subscriptions/99")
            .exchange()

        response.expectStatus().isOk()
        response.expectBody(SaasSubscription::class.java)
            .value { subscription -> assertThat(subscription).isEqualTo(SaasSubscription(99L, "Advanced", 2900)) }
    }

    @Test
    fun shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        val response = restTestClient.get()
            .uri("/subscriptions/1000")
            .exchange()

        response.expectStatus().isNotFound()
        response.expectBody().isEmpty()
    }
}
