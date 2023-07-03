package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.token.generator.RefreshTokenGenerator
import io.micronaut.security.endpoints.TokenRefreshRequest
import io.micronaut.security.token.render.BearerAccessRefreshToken
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.BAD_REQUEST

class RefreshTokenRevokedSpec extends Specification {

    @AutoCleanup
    @Shared
    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer, [:])

    @Shared
    HttpClient client = embeddedServer.applicationContext.createBean(HttpClient, embeddedServer.URL)

    @Shared
    RefreshTokenGenerator refreshTokenGenerator = embeddedServer.applicationContext.getBean(RefreshTokenGenerator)

    @Shared
    RefreshTokenRepository refreshTokenRepository = embeddedServer.applicationContext.getBean(RefreshTokenRepository)

    void 'Accessing a secured URL without authenticating returns unauthorized'() {
        given:
        Authentication user = Authentication.build("sherlock")

        when:
        String refreshToken = refreshTokenGenerator.createKey(user)
        Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken)

        then:
        refreshTokenOptional.isPresent()

        when:
        String signedRefreshToken = refreshTokenOptional.get()
        refreshTokenRepository.save(user.name, refreshToken, true) // <1>

        then:
        refreshTokenRepository.count() == old(refreshTokenRepository.count()) + 1

        when:
        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken)
        Argument<Map> errorArgument = Argument.of(Map)
        client.toBlocking().exchange(
                HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, signedRefreshToken)),
                bodyArgument,
                errorArgument)

        then:
        HttpClientResponseException e = thrown()
        e.status == BAD_REQUEST

        when:
        Optional<Map> mapOptional = e.response.getBody(Map)

        then:
        mapOptional.isPresent()

        when:
        Map m = mapOptional.get()

        then:
        m.error == 'invalid_grant'
        m.error_description == 'refresh token revoked'

        cleanup:
        refreshTokenRepository.deleteAll()
    }
}
