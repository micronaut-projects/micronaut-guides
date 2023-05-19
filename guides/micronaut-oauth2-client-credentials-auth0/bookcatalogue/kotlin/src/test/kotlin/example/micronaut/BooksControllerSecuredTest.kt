package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class BooksControllerSecuredTest(@Client("/") val httpClient: HttpClient) {

    @Test
    fun testBooksControllerIsSecured() {
        val e: HttpClientResponseException = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange(
                HttpRequest.GET<Any>("/books"),
                Boolean::class.java
            )
        }
        assertEquals(UNAUTHORIZED, e.status)
    }
}
