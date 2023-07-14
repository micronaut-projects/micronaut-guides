package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.endpoints.TokenRefreshRequest
import io.micronaut.security.token.render.AccessRefreshToken
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest(rollback = false)
internal class OauthAccessTokenTest(@Client("/") val client: HttpClient) {

    @Inject
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @Test
    fun verifyJWTAccessTokenRefreshWorks() {
        val username = "sherlock"

        val creds = UsernamePasswordCredentials(username, "password")
        val request: HttpRequest<*> = HttpRequest.POST("/login", creds)

        val oldTokenCount = refreshTokenRepository.count()
        val rsp: BearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        Thread.sleep(3000)
        assertEquals(oldTokenCount + 1, refreshTokenRepository.count())

        assertNotNull(rsp.accessToken)
        assertNotNull(rsp.refreshToken)

        Thread.sleep(1000) // sleep for one second to give time for the issued at `iat` Claim to change
        val refreshResponse = client.toBlocking().retrieve(HttpRequest.POST("/oauth/access_token",
                TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, rsp.refreshToken)), AccessRefreshToken::class.java) // <1>

        assertNotNull(refreshResponse.accessToken)
        assertNotEquals(rsp.accessToken, refreshResponse.accessToken) // <2>

        refreshTokenRepository.deleteAll()
    }
}
