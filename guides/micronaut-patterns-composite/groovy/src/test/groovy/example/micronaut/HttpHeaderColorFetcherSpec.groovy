package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'spec.name', value = 'HttpHeaderColorFetcherTest') // <1>
@MicronautTest // <2>
class HttpHeaderColorFetcherSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    void theHttpHeaderColorFetcherFetchesFromColorHeader() {
        when:
        BlockingHttpClient client = httpClient.toBlocking()

        then:
        'yellow' == client.retrieve(HttpRequest.GET('/header/color').header('color', 'yellow'))

        when:
        client.retrieve(HttpRequest.GET('/header/color'))

        then:
        thrown(HttpClientResponseException)
    }

    @Requires(property = 'spec.name', value = 'HttpHeaderColorFetcherTest') // <1>
    @Controller('/header')
    static class HttpHeaderColorFetcherTestController {

        private final HttpHeaderColorFetcher colorFetcher

        HttpHeaderColorFetcherTestController(HttpHeaderColorFetcher colorFetcher) {
            this.colorFetcher = colorFetcher
        }

        @Produces(MediaType.TEXT_PLAIN)
        @Get('/color')
        Optional<String> index(HttpRequest<?> request) {
            return colorFetcher.favouriteColor(request)
        }
    }
}
