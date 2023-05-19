package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

@MicronautTest // <1>
class BasicAuthTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun verifyHttpBasicAuthWorks() {
        //when: 'Accessing a secured URL without authenticating'
        val e = Executable { client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(TEXT_PLAIN)) } // <3>

        // then: 'returns unauthorized'
        val thrown = assertThrows(HttpClientResponseException::class.java, e) // <4>
        assertEquals(UNAUTHORIZED, thrown.status)

        assertTrue(thrown.response.headers.contains("WWW-Authenticate"))
        assertEquals("Basic realm=\"Micronaut Guide\"", thrown.response.headers.get("WWW-Authenticate"))

        //when: 'A secured URL is accessed with Basic Auth'
        val rsp = client.toBlocking().exchange(HttpRequest.GET<Any>("/")
                .accept(TEXT_PLAIN)
                .basicAuth("sherlock", "password"), // <5>
                String::class.java) // <6>
        //then: 'the endpoint can be accessed'
        assertEquals(OK, rsp.status)
        assertEquals("sherlock", rsp.body.get()) // <7>
    }
}
