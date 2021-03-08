package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UserDetails
import io.micronaut.security.token.generator.RefreshTokenGenerator
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class RefreshTokenNotFoundSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

    @Inject
    RefreshTokenGenerator refreshTokenGenerator

    void 'Accessing a secured URL without authenticating returns unauthorized'() {
        given:
        UserDetails user = new UserDetails("sherlock", [])
        when:
        String refreshToken = refreshTokenGenerator.createKey(user)
        Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken)

        then:
        refreshTokenOptional.isPresent()

        when:
        String signedRefreshToken = refreshTokenOptional.get()  // <1>
        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken)
        Argument<Map> errorArgument = Argument.of(Map)
        HttpRequest req = HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(signedRefreshToken))
        client.toBlocking().exchange(req, bodyArgument, errorArgument)

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST

        when:
        Optional<Map> mapOptional = e.response.getBody(Map)

        then:
        mapOptional.isPresent()

        when:
        Map m = mapOptional.get()

        then:
        m.error == 'invalid_grant'
        m.error_description == 'refresh token not found'
    }
}