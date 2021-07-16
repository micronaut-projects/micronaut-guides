package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.generator.claims.JwtClaims
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.security.token.jwt.validator.JwtTokenValidator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher
import spock.lang.Shared
import spock.lang.Specification

import jakarta.inject.Inject

import static io.micronaut.http.HttpMethod.POST
import static io.micronaut.http.MediaType.APPLICATION_JSON_TYPE

@MicronautTest // <1>
class LoginLdapSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client // <2>

    @Shared
    @Inject
    JwtTokenValidator tokenValidator // <3>

    void '/login with valid credentials returns 200 and access token and refresh token'() {
        when:
        HttpRequest request = HttpRequest.create(POST, '/login')
            .accept(APPLICATION_JSON_TYPE)
            .body(new UsernamePasswordCredentials('euler', 'password')) // <4>
        HttpResponse<AccessRefreshToken> rsp = client.toBlocking().exchange(request, AccessRefreshToken)

        then:
        rsp.status.code == 200
        rsp.body.present
        rsp.body.get().accessToken
    }

    void '/login with invalid credentials returns UNAUTHORIZED'() {
        when:
        HttpRequest request = HttpRequest.create(POST, '/login')
            .accept(APPLICATION_JSON_TYPE)
            .body(new UsernamePasswordCredentials('euler', 'bogus')) // <4>
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        e.status.code == 401 // <5>
    }

    void 'access token contains expiration date'() {
        when:
        HttpRequest request = HttpRequest.create(POST, '/login')
            .accept(APPLICATION_JSON_TYPE)
            .body(new UsernamePasswordCredentials('euler', 'password')) // <4>
        HttpResponse<AccessRefreshToken> rsp = client.toBlocking().exchange(request, AccessRefreshToken)

        then:
        rsp.status.code == 200
        rsp.body.present

        when:
        String accessToken = rsp.body.get().accessToken

        then:
        accessToken

        when:
        Publisher authentication = tokenValidator.validateToken(accessToken, request) // <6>

        then:
        Flux.from(authentication).blockingFirst()

        and: 'access token contains an expiration date'
        Flux.from(authentication).blockingFirst().attributes.get(JwtClaims.EXPIRATION_TIME)
    }
}
