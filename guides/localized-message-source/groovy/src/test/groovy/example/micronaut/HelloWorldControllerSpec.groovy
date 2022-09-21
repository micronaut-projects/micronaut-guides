package example.micronaut

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class HelloWorldControllerSpec extends Specification {

    @Inject
    @Client('/') // <2>
    HttpClient httpClient

    void useOfLocalizedMessageSource() {
        when:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET('/')
                .header(HttpHeaders.ACCEPT_LANGUAGE, 'es') // <3>

        then:
        'Hola Mundo' == client.retrieve(request)

        when:
        request = HttpRequest.GET('/')

        then:
        'Hello World' == client.retrieve(request)
    }
}
