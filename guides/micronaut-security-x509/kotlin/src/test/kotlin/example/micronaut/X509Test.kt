package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class X509Test {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <2>

    @Test
    fun testClientCert() {
        val response = httpClient.toBlocking().retrieve("/") // <3>
        val expected = "Hello myusername (X.509 cert issued by CN=micronaut.guide.x509)"
        assertEquals(expected, response) // <4>
    }
}
