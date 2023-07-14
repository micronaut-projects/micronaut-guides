package example.micronaut

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.OK
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class JwtAuthenticationTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        val e = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(TEXT_PLAIN)) // <3>
        }
        assertEquals(UNAUTHORIZED, e.status) // <3>
    }

    @Test
    fun uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() {
        val creds = UsernamePasswordCredentials("sherlock", "password")
        val request: HttpRequest<*> = HttpRequest.POST("/login", creds) // <4>
        val rsp: HttpResponse<BearerAccessRefreshToken> =
            client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java) // <5>
        assertEquals(OK, rsp.status)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = rsp.body()
        assertEquals("sherlock", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)

        val accessToken: String = bearerAccessRefreshToken.accessToken
        val requestWithAuthorization = HttpRequest.GET<Any>("/")
            .accept(TEXT_PLAIN)
            .bearerAuth(accessToken) // <6>
        val response: HttpResponse<String> = client.toBlocking().exchange(requestWithAuthorization, String::class.java)

        assertEquals(OK, rsp.status)
        assertEquals("sherlock", response.body()) // <7>
    }
}
