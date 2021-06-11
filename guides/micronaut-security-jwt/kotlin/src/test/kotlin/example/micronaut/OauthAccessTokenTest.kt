package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest(rollback = false)
internal class OauthAccessTokenTest {

    @Inject
    @field:Client("/")
    var client: RxHttpClient? = null

    @Inject
    var refreshTokenRepository: RefreshTokenRepository? = null

    @Test
    @Throws(InterruptedException::class)
    fun verifyJWTAccessTokenRefreshWorks() {
        val username = "sherlock"
        val creds = UsernamePasswordCredentials(username, "password")
        val request: HttpRequest<Any> = HttpRequest.POST("/login", creds)
        val oldTokenCount = refreshTokenRepository!!.count()
        val rsp: BearerAccessRefreshToken =
            client!!.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        Thread.sleep(3000)
        Assertions.assertEquals(oldTokenCount + 1, refreshTokenRepository!!.count())
        Assertions.assertNotNull(rsp.accessToken)
        Assertions.assertNotNull(rsp.refreshToken)

        Thread.sleep(1000) // sleep for one second to give time for the issued at `iat` Claim to change
        val refreshResponse = client!!.toBlocking().retrieve(
            HttpRequest.POST(
                "/oauth/access_token",
                TokenRefreshRequest(rsp.refreshToken)
            ), AccessRefreshToken::class.java // <1>
        )

        Assertions.assertNotNull(refreshResponse.accessToken)
        Assertions.assertNotEquals(rsp.accessToken, refreshResponse.accessToken) // <2>
        refreshTokenRepository!!.deleteAll()
    }
}
