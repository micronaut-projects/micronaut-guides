package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.NOT_FOUND
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import io.micronaut.core.util.StringUtils
import io.micronaut.context.annotation.Property

@Property(name = "micronaut.security.enabled", value= StringUtils.FALSE)
@MicronautTest
class BooksControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun testBooksController() {
        val rsp = httpClient.toBlocking().exchange(
                HttpRequest.GET<Any>("/books/stock/1491950358"), Boolean::class.java)
        assertEquals(OK, rsp.status())
        assertTrue(rsp.body())
    }

    @Test
    fun testBooksControllerWithNonExistingIsbn() {
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange(HttpRequest.GET<Any>("/books/stock/XXXXX"), Boolean::class.java)
        }
        assertEquals(NOT_FOUND, thrown.response.status)
    }
}
