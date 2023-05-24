package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import io.micronaut.core.util.StringUtils
import io.micronaut.context.annotation.Property

@Property(name = "micronaut.security.enabled", value= StringUtils.FALSE)
@MicronautTest // <1>
class BooksControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testRetrieveBooks() {
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/books") // <3>
        val books = client.toBlocking().retrieve(request, Argument.listOf(Book::class.java)) // <4>

        assertEquals(3, books.size)
        assertTrue(books.contains(Book("1491950358", "Building Microservices")))
        assertTrue(books.contains(Book("1680502395", "Release It!")))
    }
}
