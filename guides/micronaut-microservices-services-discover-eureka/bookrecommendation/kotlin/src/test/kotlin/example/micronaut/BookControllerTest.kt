package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable

@MicronautTest
class BookControllerTest(@Client("/") val client: HttpClient) {

    @DisabledIfEnvironmentVariable(named = "CI", matches = "true")
    @Test
    fun testRetrieveBooks() {
        val books = client.toBlocking().retrieve(HttpRequest.GET<Any>("/books"),
            Argument.listOf(BookRecommendation::class.java))
        assertEquals(1, books.size)
        assertEquals("Building Microservices", books[0].name)
    }
}
