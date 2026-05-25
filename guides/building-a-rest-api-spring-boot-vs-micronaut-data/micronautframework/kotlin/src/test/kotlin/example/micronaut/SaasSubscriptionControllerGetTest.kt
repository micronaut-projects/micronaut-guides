package example.micronaut

import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"]) // <1>
@MicronautTest // <2>
class SaasSubscriptionControllerGetTest(@param:Client("/") private val httpClient: HttpClient) { // <3>

    @Test
    fun shouldReturnASaasSubscriptionWhenDataIsSaved() {
        val client = httpClient.toBlocking()
        val response: HttpResponse<String> = client.exchange("/subscriptions/99", String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val id: Number = documentContext.read("$.id")
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(99)

        val name: String = documentContext.read("$.name")
        assertThat(name).isNotNull()
        assertThat(name).isEqualTo("Advanced")

        val cents: Int = documentContext.read("$.cents")
        assertThat(cents).isEqualTo(2900)
    }

    @Test
    fun shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        val client = httpClient.toBlocking()
        val thrown = assertThrows<HttpClientResponseException> { // <4>
            client.exchange("/subscriptions/1000", String::class.java)
        }
        assertThat(thrown.status.code).isEqualTo(HttpStatus.NOT_FOUND.code)
        assertThat(thrown.response.body).isEmpty()
    }
}
