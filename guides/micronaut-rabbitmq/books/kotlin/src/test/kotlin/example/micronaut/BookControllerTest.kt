package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.internal.verification.VerificationModeFactory.times
import java.util.Optional
import jakarta.inject.Inject

@MicronautTest
class BookControllerTest(@Client("/") val client: HttpClient) {

    @Inject
    lateinit var analyticsClient: AnalyticsClient

    @Test
    fun testMessageIsPublishedToRabbitMQWhenBookFound() {
        val result = retrieveGet("/books/1491950358")
        assertNotNull(result)
        Mockito.verify(analyticsClient, times(1)).updateAnalytics(result!!)
    }

    @Test
    fun testMessageIsNotPublishedToRabbitMQWhenBookNotFound() {
        assertThrows(HttpClientResponseException::class.java) {
            retrieveGet("/books/INVALID")
        }
        Mockito.verifyNoInteractions(analyticsClient)
    }

    @Primary
    @MockBean
    fun analyticsClient(): AnalyticsClient {
        return Mockito.mock(AnalyticsClient::class.java)
    }

    private fun retrieveGet(url: String): Book? {
        return client.toBlocking().retrieve(
            HttpRequest.GET<Any>(url),
            Argument.of(Book::class.java)
        )
    }
}
