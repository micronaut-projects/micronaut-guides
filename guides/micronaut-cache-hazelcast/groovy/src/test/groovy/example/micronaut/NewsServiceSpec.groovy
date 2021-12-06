package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.GenericContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Timeout

import java.time.Month

@Stepwise // <1>
@MicronautTest(startApplication = false) // <2>
@Testcontainers // <3>
class NewsServiceSpec extends Specification
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

    @Inject // <6>
    NewsService newsService;

    @Timeout(12) // <7>
    void "first invocation of November does not hit cache"() {
        when:
        List<String> headlines = newsService.headlines(Month.NOVEMBER)

        then:
        2 == headlines.size()
    }

    @Timeout(1) // <7>
    void "second invocation of November hits Cache"() {
        when:
        List<String> headlines = newsService.headlines(Month.NOVEMBER)

        then:
        2 == headlines.size()
    }

    @Timeout(4) // <7>
    void "first invocation of October does not hit Cache"() {
        when:
        List<String> headlines = newsService.headlines(Month.OCTOBER)

        then:
        1 == headlines.size()
    }

    @Timeout(1) // <7>
    void "second invocation of october hits cache"() {
        when:
        List<String> headlines = newsService.headlines(Month.OCTOBER)

        then:
        1 == headlines.size()
    }

    @Timeout(1) // <7>
    void "adding a headline to november updates cache"() {
        when:
        List<String> headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")

        then:
        3 == headlines.size()
    }

    @Timeout(1) // <7>
    void "november cache was updated by cache put and thus the value is retrieved from the cache"() {
        when:
        List<String> headlines = newsService.headlines(Month.NOVEMBER)

        then:
        3 == headlines.size()
    }

    @Timeout(1) // <7>
    void "invalidate november cache with cache invalidate"() {
        when:
        newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")

        then:
        noExceptionThrown()
    }

    @Timeout(1) // <7>
    void "october cache is still valid"() {
        when:
        List<String> headlines = newsService.headlines(Month.OCTOBER)

        then:
        1 == headlines.size()
    }

    @Timeout(4) // <7>
    void "november cache was invalidated"() {
        when:
        List<String> headlines = newsService.headlines(Month.NOVEMBER)

        then:
        2 == headlines.size()
    }
}
