package example.micronaut

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class DeclarativeHttpClientWithJwtSpec extends Specification {

    @Inject
    AppClient appClient // <1>

    void "Verify JWT authentication works with declarative @Client"() {
        when: 'Login endpoint is called with valid credentials'
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("sherlock", "password")
        BearerAccessRefreshToken loginRsp = appClient.login(creds) // <2>

        then:
        loginRsp
        loginRsp.accessToken
        JWTParser.parse(loginRsp.accessToken) instanceof SignedJWT

        when:
        String msg = appClient.home("Bearer $loginRsp.accessToken") // <3>

        then:
        msg == 'sherlock'
    }
}
