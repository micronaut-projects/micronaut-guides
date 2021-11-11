package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import reactor.core.publisher.Flux

@MicronautTest
class BookControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: StreamingHttpClient

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    fun testRetrieveBooks() {
        val books = Flux
                .from(client.jsonStream(HttpRequest.GET<Any>("/books"), BookRecommendation::class.java))
                .collectList()
                .block()
        assertEquals(1, books.size)
        assertEquals("Building Microservices", books[0].name)
    }
}
