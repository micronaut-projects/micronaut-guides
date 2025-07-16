package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Timeout
import jakarta.inject.Inject
import java.time.Month

@Stepwise // <1>
@MicronautTest // <2>
class NewsControllerSpec extends Specification
        implements TestPropertyProvider { // <3>

    @Override
    @NonNull
    Map<String, String> getProperties() { // <3>
        ["hazelcast.client.network.addresses": HazelcastUtils.url]
    }

    def cleanupSpec() {
        HazelcastUtils.close()
    }

    @Shared
    String expected = "Micronaut AOP: Awesome flexibility without the complexity"

    @Inject
    @Client("/") // <4>
    HttpClient client

    @Timeout(30) // <5>
    void "fetching october headlines first time does not use cache"() {
        when:
        News news = client.toBlocking().retrieve(createRequest(), News)

        then:
        [expected] == news.headlines
    }

    @Timeout(1) // <5>
    void "fetching october headlines second time uses cache"() {
        when:
        News news = client.toBlocking().retrieve(createRequest(), News)

        then:
        [expected] == news.headlines
    }

    private static HttpRequest<?> createRequest() {
        HttpRequest.GET(UriBuilder.of("/")
                .path(Month.OCTOBER.name())
                .build())
    }
}
