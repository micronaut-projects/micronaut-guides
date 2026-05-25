package example.micronaut

import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest
class SaasSubscriptionControllerGetTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun shouldReturnASaasSubscriptionWhenDataIsSaved() {
        val client = httpClient.toBlocking()
        val response: HttpResponse<String> = client.exchange("/subscriptions/99", String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val id = documentContext.read<Number>("$.id")
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(99)

        val name = documentContext.read<String>("$.name")
        assertThat(name).isNotNull()
        assertThat(name).isEqualTo("Advanced")

        val cents = documentContext.read<Int>("$.cents")
        assertThat(cents).isEqualTo(2900)
    }

    @Test
    fun shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        val client = httpClient.toBlocking()
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            client.exchange("/subscriptions/1000", String::class.java)
        }
        assertThat(thrown.status.code).isEqualTo(HttpStatus.NOT_FOUND.code)
        assertThat(thrown.response.getBody(String::class.java)).isNotEmpty()
    }
}
