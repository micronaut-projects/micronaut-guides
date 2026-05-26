package example.micronaut

import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI

@MicronautTest // <1>
class SaasSubscriptionPostControllerTest {

    @Test
    fun shouldCreateANewSaasSubscription(@Client("/") httpClient: HttpClient) { // <2>
        val client = httpClient.toBlocking()
        val subscription = SaasSubscription(100L, "Advanced", 2900)

        val createResponse = client.exchange(HttpRequest.POST("/subscriptions", subscription), Void::class.java)
        assertThat(createResponse.status.code).isEqualTo(HttpStatus.CREATED.code)
        val locationOfNewSaasSubscriptionOptional = createResponse.headers.get(HttpHeaders.LOCATION, URI::class.java)
        assertThat(locationOfNewSaasSubscriptionOptional).isPresent()

        val locationOfNewSaasSubscription = locationOfNewSaasSubscriptionOptional.get()
        val getResponse = client.exchange(HttpRequest.GET<Any>(locationOfNewSaasSubscription), String::class.java)
        assertThat(getResponse.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(getResponse.body())
        val id = documentContext.read<Number>("$.id")
        assertThat(id).isNotNull()

        val cents = documentContext.read<Int>("$.cents")
        assertThat(cents).isEqualTo(2900)
    }
}
