package example.micronaut

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class LoginIncludesRefreshTokenTest(@Client("/") val client: HttpClient) {

    @Test
    fun uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() {
        val creds = UsernamePasswordCredentials("sherlock", "password")
        val request: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val rsp: BearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)

        assertEquals("sherlock", rsp.username)
        assertNotNull(rsp.accessToken)
        assertNotNull(rsp.refreshToken) // <1>

        assertTrue(JWTParser.parse(rsp.accessToken) is SignedJWT)
    }
}
