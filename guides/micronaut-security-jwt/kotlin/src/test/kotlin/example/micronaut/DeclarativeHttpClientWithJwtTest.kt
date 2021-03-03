package example.micronaut

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class DeclarativeHttpClientWithJwtTest {

    @Inject
    lateinit var appClient : AppClient // <1>

    @Test
    fun verifyJwtAuthenticationWorksWithDeclarativeClient() {
        val creds: UsernamePasswordCredentials = UsernamePasswordCredentials("sherlock", "password")
        val loginRsp : BearerAccessRefreshToken  = appClient.login(creds) // <2>

        assertNotNull(loginRsp)
        assertNotNull(loginRsp.accessToken)
        assertTrue(JWTParser.parse(loginRsp.accessToken) is SignedJWT)

        val msg = appClient.home("Bearer ${loginRsp.accessToken}") // <3>

        assertEquals("sherlock", msg)
    }
}