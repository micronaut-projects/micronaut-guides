package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest
class UserControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    void testUserEndpointIsSecured() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/user'))

        then:
        HttpClientResponseException e = thrown()
        UNAUTHORIZED == e.response.status
    }
}
