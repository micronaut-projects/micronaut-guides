package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

@MicronautTest
internal class UnsignedRefreshTokenTest {

    @Inject
    @field:Client("/")
    var client: RxHttpClient? = null

    @Test
    fun accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {
        val unsignedRefreshedToken = "foo" // <1>
        val bodyArgument = Argument.of(BearerAccessRefreshToken::class.java)
        val errorArgument = Argument.of(Map::class.java)
        val e = Assertions.assertThrows(HttpClientResponseException::class.java) {
            client!!.toBlocking().exchange(
                HttpRequest.POST("/oauth/access_token", TokenRefreshRequest(unsignedRefreshedToken)),
                bodyArgument,
                errorArgument
            )
        }
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, e.status)

        val mapOptional: Optional<Map<*, *>> = e.response.getBody(Map::class.java)
        Assertions.assertTrue(mapOptional.isPresent)

        val m = mapOptional.get()
        Assertions.assertEquals("invalid_grant", m["error"])
        Assertions.assertEquals("Refresh token is invalid", m["error_description"])
    }
}
