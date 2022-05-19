package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class FruitValidationControllerTest(
    @Inject @Client("/") val httpClient: HttpClient // <2>
) : BaseTest() {

    @Test
    fun fruitIsValidated() {
        val exception = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<FruitCommand, Any>(
                HttpRequest.POST("/fruits", FruitCommand("", ""))
            )
        }
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }
}
