package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest // <1>
class UserControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client // <2>

    void testUserEndpointIsSecured() { // <3>
        when:
        client.toBlocking().exchange(HttpRequest.GET('/user'))

        then:
        HttpClientResponseException e = thrown()
        UNAUTHORIZED == e.response.status
    }

    void testAuthenticatedCanFetchUsername() {
        when:
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials('sherlock', 'password')
        HttpRequest<?> request = HttpRequest.POST('/login', credentials)

        BearerAccessRefreshToken bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken)

        String username = client.toBlocking().retrieve(HttpRequest.GET('/user')
                .header('Authorization', 'Bearer ' + bearerAccessRefreshToken.getAccessToken()), String)

        then:
        'sherlock' == username
    }
}
