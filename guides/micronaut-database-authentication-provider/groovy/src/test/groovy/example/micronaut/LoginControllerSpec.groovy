package example.micronaut

import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.security.token.jwt.validator.JwtTokenValidator
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.reactivex.Flowable
import spock.lang.Shared
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class LoginControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    @Shared
    @Inject
    JwtTokenValidator tokenValidator

    @Inject
    UserGormService userGormService

    void 'attempt to access /login without supplying credentials server responds BAD REQUEST'() {
        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/login')
            .accept(MediaType.APPLICATION_JSON_TYPE)
        client.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        e.status.code == 400
    }

    void '/login with valid credentials for a database user returns 200 and access token'() {
        expect:
        userGormService.count() > 0

        when:
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/login')
            .accept(MediaType.APPLICATION_JSON_TYPE)
            .body(new UsernamePasswordCredentials('sherlock', 'elementary'))
        HttpResponse<AccessRefreshToken> rsp = client.toBlocking().exchange(request, AccessRefreshToken)

        then:
        noExceptionThrown()
        rsp.status.code == 200
        rsp.body.isPresent()
        rsp.body.get().accessToken

        when:
        String accessToken = rsp.body.get().accessToken
        Authentication authentication = Flowable.fromPublisher(tokenValidator.validateToken(accessToken)).blockingFirst()

        then:
        authentication.getAttributes()
        authentication.getAttributes().containsKey('roles')
        authentication.getAttributes().containsKey('iss')
        authentication.getAttributes().containsKey('exp')
        authentication.getAttributes().containsKey('iat')
    }
}
