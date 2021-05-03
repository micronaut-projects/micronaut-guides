package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Timeout

import javax.inject.Inject
import java.time.Month

@MicronautTest
class NewsControllerSpec extends Specification {

    @Inject
    EmbeddedServer server

    @Inject
    @Client("/")
    HttpClient client

    @Timeout(4) // <1>
    void "fetching october headlines uses cache"() {
        given:
        String expected = "Micronaut AOP: Awesome flexibility without the complexity"

        when:
        HttpRequest request = HttpRequest.GET(UriBuilder.of("/")
                .path(Month.OCTOBER.name())
                .build())
        News news = client.toBlocking().retrieve(request, News)

        then:
        [expected] == news.headlines

        when:
        news = client.toBlocking().retrieve(request, News)

        then:
        [expected] == news.headlines
    }
}
