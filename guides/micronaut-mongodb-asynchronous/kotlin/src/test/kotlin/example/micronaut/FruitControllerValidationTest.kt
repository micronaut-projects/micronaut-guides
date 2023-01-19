package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.BAD_REQUEST
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@Property(name = "spec.name", value = "FruitControllerValidationTest")
@MicronautTest
class FruitControllerValidationTest(@Client("/") val httpClient: HttpClient) {

    @Test
    fun testFruitIsValidated() {
        val e = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<Fruit, Any>(
                    HttpRequest.POST("/fruits", Fruit("", "Hola")))
        }
        assertEquals(BAD_REQUEST, e.status)
    }
}
