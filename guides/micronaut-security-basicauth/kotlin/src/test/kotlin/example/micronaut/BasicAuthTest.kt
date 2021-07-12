package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import jakarta.inject.Inject

@MicronautTest // <1>
class BasicAuthTest {

    @Inject
    lateinit var embeddedServer: EmbeddedServer

    @Test
    fun verifyHttpBasicAuthWorks() {
        val client : HttpClient = embeddedServer.applicationContext.createBean(HttpClient::class.java, embeddedServer.url) // <2>

        //when: 'Accessing a secured URL without authenticating'
        val e = Executable { client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(MediaType.TEXT_PLAIN)) } // <3>

        // then: 'returns unauthorized'
        val thrown = assertThrows(HttpClientResponseException::class.java, e) // <4>
        assertEquals(thrown.status, HttpStatus.UNAUTHORIZED)

        assertTrue(thrown.response.headers.contains("WWW-Authenticate"))
        assertEquals(thrown.response.headers.get("WWW-Authenticate"), "Basic realm=\"Micronaut Guide\"")

        //when: 'A secured URL is accessed with Basic Auth'
        val rsp = client.toBlocking().exchange(HttpRequest.GET<Any>("/")
                .accept(MediaType.TEXT_PLAIN)
                .basicAuth("sherlock", "password"),  // <5>
                String::class.java) // <6>
        //then: 'the endpoint can be accessed'
        assertEquals(rsp.status, HttpStatus.OK)
        assertEquals(rsp.body.get(), "sherlock") // <7>
    }
}