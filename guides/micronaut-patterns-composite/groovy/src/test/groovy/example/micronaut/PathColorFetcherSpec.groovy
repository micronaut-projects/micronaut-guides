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

@Property(name = 'spec.name', value = 'PathColorFetcherTest') // <1>
@MicronautTest // <2>
class PathColorFetcherSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    void theHttpHeaderColorFetcherFetchesFromColorHeader() {
        when:
        BlockingHttpClient client = httpClient.toBlocking()

        then:
        'mint' == client.retrieve(HttpRequest.GET('/colorpath/mint'))

        when:
        client.retrieve(HttpRequest.GET('/colorpath/foo'))

        then:
        thrown(HttpClientResponseException)
    }

    @Requires(property = 'spec.name', value = 'PathColorFetcherTest') // <1>
    @Controller('/colorpath')
    static class PathColorFetcherTestController {

        private final PathColorFetcher colorFetcher

        PathColorFetcherTestController(PathColorFetcher colorFetcher) {
            this.colorFetcher = colorFetcher
        }

        @Produces(MediaType.TEXT_PLAIN)
        @Get('/mint')
        Optional<String> index(HttpRequest<?> request) {
            return colorFetcher.favouriteColor(request)
        }

        @Produces(MediaType.TEXT_PLAIN)
        @Get('/foo')
        Optional<String> foo(HttpRequest<?> request) {
            return colorFetcher.favouriteColor(request)
        }
    }
}
