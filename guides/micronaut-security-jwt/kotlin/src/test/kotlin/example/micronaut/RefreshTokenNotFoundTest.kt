package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus.BAD_REQUEST
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.generator.RefreshTokenGenerator
import io.micronaut.security.endpoints.TokenRefreshRequest
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Optional

@MicronautTest
class RefreshTokenNotFoundTest(@Client("/") val client: HttpClient) {

    @Inject
    lateinit var refreshTokenGenerator: RefreshTokenGenerator

    @Test
    fun accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {

        val user = Authentication.build("sherlock")
        val refreshToken = refreshTokenGenerator.createKey(user)
        val refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken)
        assertTrue(refreshTokenOptional.isPresent)

        val signedRefreshToken = refreshTokenOptional.get() // <1>
        val bodyArgument = Argument.of(BearerAccessRefreshToken::class.java)
        val errorArgument = Argument.of(MutableMap::class.java)
        val req: HttpRequest<*> = HttpRequest.POST("/oauth/access_token", TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, signedRefreshToken))
        val e = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(req, bodyArgument, errorArgument)
        }
        assertEquals(BAD_REQUEST, e.status)

        val mapOptional: Optional<Map<*, *>> = e.response.getBody(Map::class.java)
        assertTrue(mapOptional.isPresent)

        val m = mapOptional.get()
        assertEquals("invalid_grant", m["error"])
        assertEquals("refresh token not found", m["error_description"])
    }
}
