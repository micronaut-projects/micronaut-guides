package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class BooksControllerTest {
    @Inject
    @field:Client("/")
    lateinit var client: StreamingHttpClient

    @Test
    fun testRetrieveBooks() {
        val books = client.jsonStream(HttpRequest.GET<Any>("/books"), BookRecommendation::class.java)
        assertEquals(books.toList().blockingGet().size, 1)
        assertEquals(books.toList().blockingGet()[0].name, "Building Microservices")
    }
}
