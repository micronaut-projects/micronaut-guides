package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class BooksControllerTest(@Client("/") val httpClient: HttpClient) {

    @Test
    fun testBooksController() {
        val rsp = httpClient.toBlocking().exchange(HttpRequest.GET<Any>("/books/stock/1491950358"), Boolean::class.java)
        assertEquals(rsp.status(), HttpStatus.OK)
        assertTrue(rsp.body()!!)
    }

    @Test
    fun testBooksControllerWithNonExistingIsbn() {
        val thrown = assertThrows(HttpClientResponseException::class.java) { httpClient.toBlocking().exchange(HttpRequest.GET<Any>("/books/stock/XXXXX"), Boolean::class.java) }
        assertEquals(
                HttpStatus.NOT_FOUND,
                thrown.response.status
        )
    }
}
