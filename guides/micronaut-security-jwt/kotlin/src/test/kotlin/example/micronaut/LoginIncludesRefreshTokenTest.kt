package example.micronaut

import io.micronaut.http.client.RxHttpClient
import kotlin.Throws
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.text.ParseException
import javax.inject.Inject

@MicronautTest
class LoginIncludesRefreshTokenTest {
    @Inject
    @field:Client("/")
    var client: RxHttpClient? = null
    @Test
    @Throws(ParseException::class)
    fun uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() {
        val creds = UsernamePasswordCredentials("sherlock", "password")
        val request: HttpRequest<Any> = HttpRequest.POST("/login", creds)
        val rsp: BearerAccessRefreshToken =
            client!!.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        Assertions.assertEquals("sherlock", rsp.username)
        Assertions.assertNotNull(rsp.accessToken)
        Assertions.assertNotNull(rsp.refreshToken) // <1>
        Assertions.assertTrue(JWTParser.parse(rsp.accessToken) is SignedJWT)
    }
}
