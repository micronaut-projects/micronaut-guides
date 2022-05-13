package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FruitValidationControllerTest : BaseMicroStreamTest() {

    @Test
    fun fruitIsValidated() {
        val exception = Assertions.assertThrows(HttpClientResponseException::class.java) {
            httpClient!!.toBlocking().exchange<FruitCommand, Any>(
                HttpRequest.POST(
                    "/fruits",
                    FruitCommand("", "")
                )
            )
        }
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.status)
    }
}
