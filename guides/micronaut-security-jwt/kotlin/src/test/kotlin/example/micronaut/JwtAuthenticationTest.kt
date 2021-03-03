package example.micronaut

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import javax.inject.Inject

@MicronautTest // <1>
class JwtAuthenticationTest {

    @Inject
    @field:Client("/")
    lateinit var client : HttpClient // <2>

    @Test
    fun accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
        val e = Executable { client.toBlocking().exchange<Any, Any>(HttpRequest.GET<Any>("/").accept(MediaType.TEXT_PLAIN)) } // <3>
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(thrown.status, HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() {
        val creds = UsernamePasswordCredentials("sherlock", "password")
        val request : HttpRequest<Any>  = HttpRequest.POST("/login", creds) // <4>
        val rsp : HttpResponse<BearerAccessRefreshToken> = client.toBlocking().exchange(request, BearerAccessRefreshToken::class.java) // <5>
        assertEquals(HttpStatus.OK, rsp.status)

        val bearerAccessRefreshToken : BearerAccessRefreshToken = rsp.body()!!  // <6>

        assertEquals("sherlock", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)

        val accessToken : String = bearerAccessRefreshToken.accessToken
        val response: HttpResponse<String> = client.toBlocking().exchange(HttpRequest.GET<Any>("/").accept(MediaType.TEXT_PLAIN).bearerAuth(accessToken), String::class.java) // <7>

        assertEquals(HttpStatus.OK, rsp.status)
        assertEquals("sherlock", response.body()!!) // <8>
    }
}
