package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest // <1>
class BasicAuthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    def "by default every endpoint is secured"() {
        when: 'Accessing a secured URL without authenticating'
        client.toBlocking().exchange(HttpRequest.GET('/')) // <3>

        then: 'returns unauthorized'
        HttpClientResponseException e = thrown() // <4>
        e.status == HttpStatus.UNAUTHORIZED
        e.response.headers.contains("WWW-Authenticate")
        e.response.headers.get("WWW-Authenticate") ==  'Basic realm=\"Micronaut Guide"'

    }

    def "Verify HTTP Basic Auth works"() {
        when: 'A secured URL is accessed with Basic Auth'
        HttpRequest request = HttpRequest.GET('/')
                .basicAuth("sherlock", "password") // <5>
        HttpResponse<String> rsp = client.toBlocking().exchange(request, String) // <6>

        then: 'the endpoint can be accessed'
        rsp.status == HttpStatus.OK
        rsp.body() == 'sherlock' // <7>
    }
}
