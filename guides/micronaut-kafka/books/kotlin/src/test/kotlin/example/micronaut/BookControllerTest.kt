package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Optional
import javax.inject.Inject

@MicronautTest
class BookControllerTest {

    private var clientInvocationCount = 0

    @Inject
    lateinit var analyticsClient: AnalyticsClient

    @Inject
    @field:Client("/")
    lateinit var client: RxHttpClient

    @Test
    fun testMessageIsPublishedToKafkaWhenBookFound() {
        val result = retrieveGet("/books/1491950358")
        assertNotNull(result)
        assertTrue(result.isPresent)
        assertEquals(1, clientInvocationCount)
    }

    @Test
    fun testMessageIsNotPublishedToKafkaWhenBookNotFound() {
        assertThrows(HttpClientResponseException::class.java) { retrieveGet("/books/INVALID") }
        assertEquals(0, clientInvocationCount)
    }

    @Primary
    @MockBean
    fun analyticsClient(): AnalyticsClient = object : AnalyticsClient {
        override fun updateAnalytics(book: Book) {
            clientInvocationCount++
        }
    }

    private fun retrieveGet(url: String) = client
            .toBlocking()
            .retrieve(HttpRequest.GET<Any>(url),
                    Argument.of(Optional::class.java, Book::class.java))
}
