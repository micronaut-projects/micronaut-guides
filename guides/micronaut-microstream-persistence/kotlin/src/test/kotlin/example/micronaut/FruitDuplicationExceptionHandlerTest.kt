package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class FruitDuplicationExceptionHandlerTest : BaseTest() {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <3>

    @Test
    fun duplicatedFruitsReturns400() {
        val banana = FruitCommand("Banana")
        val request = HttpRequest.POST("/fruits", banana)
        val response = httpClient.toBlocking().exchange<FruitCommand, Any>(request)
        assertEquals(HttpStatus.CREATED, response.status())
        val exception = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<FruitCommand, Any>(request)
        }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.status)

        val deleteRequest = HttpRequest.DELETE("/fruits", banana)
        val deleteResponse = httpClient.toBlocking().exchange<FruitCommand, Any>(deleteRequest)
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status())
    }
}