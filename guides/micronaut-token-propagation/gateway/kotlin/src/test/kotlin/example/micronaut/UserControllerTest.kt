package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest
class UserControllerTest(@Client("/") val client: HttpClient) {

    @Test
    fun testUserEndpointIsSecured() {
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET("/user"))
        }
        assertEquals(UNAUTHORIZED, thrown.response.status)
    }

    @Test
    fun testAuthenticatedCanFetchUsername() {
        val credentials = UsernamePasswordCredentials("sherlock", "password")
        val request: HttpRequest<*> = HttpRequest.POST("/login", credentials)
        val bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        val username = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/user")
                .bearerAuth(bearerAccessRefreshToken.accessToken), String::class.java)
        assertEquals("sherlock", username)
    }
}
