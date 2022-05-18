package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class ColorControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient // <2>

    void testCompositePattern() {
        when:
        BlockingHttpClient client = httpClient.toBlocking()

        then:
        'yellow' == client.retrieve(HttpRequest.GET('/color').header('color', 'yellow'))
        'yellow' == client.retrieve(HttpRequest.GET('/color/mint').header('color', 'yellow'))
        'mint' == client.retrieve(HttpRequest.GET('/color/mint'))

        when:
        client.retrieve(HttpRequest.GET('/color'))

        then:
        thrown(HttpClientResponseException)
    }
}
