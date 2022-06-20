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
import jakarta.inject.Inject
import java.util.concurrent.ConcurrentLinkedDeque

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

@MicronautTest // <1> <2>
class BookControllerSpec extends Specification implements TestPropertyProvider { // <3>

    private static final Collection<Book> received = new ConcurrentLinkedDeque<>()

    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse('confluentinc/cp-kafka:latest')) // <4>

    @Inject
    AnalyticsListener analyticsListener // <5>

    @Inject
    @Client('/')
    HttpClient client // <6>

    void 'test message is published to Kafka when book found'() {
        when:
        String isbn = '1491950358'
        Optional<Book> result = retrieveGet('/books/' + isbn) // <7>

        then:
        result != null
        result.present
        isbn == result.get().isbn

        new PollingConditions(timeout: 5).eventually { // <8>
            !received.isEmpty()
            1 == received.size() // <9>
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
        sleep 5000 // <10>

        then:
        0 == received.size()
    }

    void cleanup() {
        received.clear()
    }

    @Override
    Map<String, String> getProperties() {
        kafka.start()
        ['kafka.bootstrap.servers': kafka.bootstrapServers] // <11>
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class AnalyticsListener {

        @Topic('analytics')
        void updateAnalytics(Book book) {
            received << book
        }
    }

    private Optional<Book> retrieveGet(String url) {
        client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional, Book))
    }
}
