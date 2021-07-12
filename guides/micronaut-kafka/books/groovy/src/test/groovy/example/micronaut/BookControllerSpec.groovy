package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import javax.inject.Inject
import java.util.concurrent.ConcurrentLinkedDeque

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

@MicronautTest
class BookControllerSpec extends Specification implements TestPropertyProvider {

    private static final Collection<Book> received = new ConcurrentLinkedDeque<>();
    private static final PollingConditions conditions = new PollingConditions(timeout: 5)

    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse('confluentinc/cp-kafka:latest'))

    @Inject
    AnalyticsListener analyticsListener

    @Inject
    @Client('/')
    HttpClient client

    void 'test message is published to Kafka when book found'() {
        when:
        String isbn = '1491950358'
        Optional<Book> result = retrieveGet('/books/' + isbn)

        then:
        result != null
        result.present
        isbn == result.get().isbn

        conditions.eventually {
            !received.isEmpty()
            1 == received.size()
        }

        when:
        Book bookFromKafka = received[0]

        then:
        bookFromKafka
        isbn == bookFromKafka.isbn
    }

    void 'test message is not published to Kafka when book not found'() {
        when:
        retrieveGet '/books/INVALID'

        then:
        thrown HttpClientResponseException

        when:
        sleep 5000

        then:
        0 == received.size()
    }

    void cleanup() {
        received.clear()
    }

    @Override
    Map<String, String> getProperties() {
        kafka.start()
        ['kafka.bootstrap.servers': kafka.bootstrapServers]
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class AnalyticsListener {

        @Topic('analytics')
        void updateAnalytics(Book book) {
            received.add(book);
        }
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional, Book))
    }
}
