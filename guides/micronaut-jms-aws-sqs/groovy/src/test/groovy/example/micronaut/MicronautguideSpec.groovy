package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest
class MicronautguideSpec extends Specification implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient client

    @Inject
    DemoConsumer demoConsumer

    void 'test it works'() {
        when:
        int messageCount = demoConsumer.getMessageCount()

        then:
        messageCount == 0

        when:
        HttpResponse<?> response = client.toBlocking().exchange(HttpRequest.POST('/demo', [:]))

        then:
        new PollingConditions(initialDelay: 3, timeout: 10).eventually {
            demoConsumer.getMessageCount() == 1
        }
    }

    def cleanupSpec() {
        LocalStack.close()
    }

    @Override
    Map<String, String> getProperties() {
        LocalStack.getProperties()
    }
}
