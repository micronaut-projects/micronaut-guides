package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Optional
import javax.inject.Inject

@MicronautTest
internal class UnsignedRefreshTokenTest {

    @Inject
    @field:Client("/")
    var client: HttpClient? = null

    @Test
    fun accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {
        val unsignedRefreshedToken = "foo" // <1>
        val bodyArgument = Argument.of(BearerAccessRefreshToken::class.java)
        val errorArgument = Argument.of(Map::class.java)
        val e = assertThrows(HttpClientResponseException::class.java) {
            client!!.toBlocking().exchange(
                HttpRequest.POST("/oauth/access_token", TokenRefreshRequest(unsignedRefreshedToken)),
                bodyArgument,
                errorArgument
            )
        }
        assertEquals(HttpStatus.BAD_REQUEST, e.status)

        val mapOptional: Optional<Map<*, *>> = e.response.getBody(Map::class.java)
        assertTrue(mapOptional.isPresent)

        val m = mapOptional.get()
        assertEquals("invalid_grant", m["error"])
        assertEquals("Refresh token is invalid", m["error_description"])
    }
}
