package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable
import reactor.core.publisher.Flux

@MicronautTest
class BooksControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client:HttpClient

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    fun testRetrieveBooks() {
        val books = client.toBlocking().retrieve(HttpRequest.GET<Any>("/books"),
            Argument.listOf(BookRecommendation::class.java))
        assertEquals(books.size, 1)
        assertEquals(books[0].name, "Building Microservices")
    }
}
