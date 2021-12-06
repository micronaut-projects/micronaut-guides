package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Timeout
import jakarta.inject.Inject
import java.time.Month

@Stepwise // <1>
@MicronautTest // <2>
@Testcontainers // <3>
class NewsControllerSpec extends Specification
        implements TestPropertyProvider { // <4>

    @Shared // <5>
    GenericContainer hazelcast = new GenericContainer("hazelcast/hazelcast:4.2.1")
            .withExposedPorts(5701)

    @Override
    @NonNull
    Map<String, String> getProperties() { // <4>
        if (!hazelcast.isRunning()) {
            hazelcast.start()
        }
        ["hazelcast.client.network.addresses": "127.0.0.1:$hazelcast.firstMappedPort"]
    }

    @Shared
    String expected = "Micronaut AOP: Awesome flexibility without the complexity"

    @Inject
    @Client("/") // <6>
    HttpClient client

    @Timeout(12) // <7>
    void "fetching october headlines first time does not use cache"() {
        when:
        News news = client.toBlocking().retrieve(createRequest(), News)

        then:
        [expected] == news.headlines
    }

    @Timeout(1) // <7>
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
