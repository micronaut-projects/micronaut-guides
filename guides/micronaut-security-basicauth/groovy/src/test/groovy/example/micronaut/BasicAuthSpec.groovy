package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest // <1>
class BasicAuthSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client // <2>

    def "by default every endpoint is secured"() {
        when: 'Accessing a secured URL without authenticating'
        client.toBlocking().exchange(HttpRequest.GET('/')) // <3>

        then: 'returns unauthorized'
        HttpClientResponseException e = thrown() // <4>
        e.status == HttpStatus.UNAUTHORIZED
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
