package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import jakarta.inject.Inject
import java.util.concurrent.ConcurrentLinkedDeque

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

@MicronautTest // <1>
class BookControllerSpec extends Specification {

    private static final Collection<Book> received = new ConcurrentLinkedDeque<>()

    @Inject
    AnalyticsListener analyticsListener // <2>

    @Inject
    @Client('/')
    HttpClient client // <3>

    void 'test message is published to Kafka when book found'() {
        when:
        String isbn = '1491950358'
        Optional<Book> result = retrieveGet('/books/' + isbn) // <4>

        then:
        result != null
        result.present
        isbn == result.get().isbn

        new PollingConditions(timeout: 5).eventually { // <5>
            !received.isEmpty()
            1 == received.size() // <6>
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
        sleep 5000 // <7>

        then:
        0 == received.size()
    }

    void cleanup() {
        received.clear()
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
