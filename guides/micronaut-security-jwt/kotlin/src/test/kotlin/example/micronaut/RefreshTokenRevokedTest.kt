package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.generator.RefreshTokenGenerator
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Optional

internal class RefreshTokenRevokedTest {
    var embeddedServer = ApplicationContext.run(EmbeddedServer::class.java, emptyMap())
    var client = embeddedServer.applicationContext.createBean(HttpClient::class.java, embeddedServer.url)
    var refreshTokenGenerator = embeddedServer.applicationContext.getBean(RefreshTokenGenerator::class.java)
    var refreshTokenRepository = embeddedServer.applicationContext.getBean(RefreshTokenRepository::class.java)

    @Test
    fun accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {
        val user = Authentication.build("sherlock")
        val refreshToken = refreshTokenGenerator.createKey(user)
        val refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken)
        assertTrue(refreshTokenOptional.isPresent)

        val oldTokenCount = refreshTokenRepository.count()
        val signedRefreshToken = refreshTokenOptional.get()
        refreshTokenRepository.save(user.username, refreshToken, true) // <1>
        assertEquals(oldTokenCount + 1, refreshTokenRepository.count())

        val bodyArgument = Argument.of(BearerAccessRefreshToken::class.java)
        val errorArgument = Argument.of(Map::class.java)
        val e = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(
                HttpRequest.POST("/oauth/access_token", TokenRefreshRequest(signedRefreshToken)),
                bodyArgument,
                errorArgument
            )
        }
        assertEquals(HttpStatus.BAD_REQUEST, e.status)

        val mapOptional: Optional<Map<*, *>> = e.response.getBody(Map::class.java)
        assertTrue(mapOptional.isPresent)

        val m = mapOptional.get()
        assertEquals("invalid_grant", m["error"])
        assertEquals("refresh token revoked", m["error_description"])
        refreshTokenRepository.deleteAll()
    }
}
