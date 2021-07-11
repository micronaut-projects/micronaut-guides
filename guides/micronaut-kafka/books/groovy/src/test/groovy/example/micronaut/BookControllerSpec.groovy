package example.micronaut

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.util.concurrent.ConcurrentLinkedDeque

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

class BookControllerSpec extends Specification {

    private static final Collection<Book> received = new ConcurrentLinkedDeque<>();
    private static final PollingConditions conditions = new PollingConditions(timeout: 5)

    @Shared
    @AutoCleanup
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse('confluentinc/cp-kafka:latest'))

    @Shared @AutoCleanup EmbeddedServer embeddedServer
    @Shared @AutoCleanup ApplicationContext context
    @Shared @AutoCleanup HttpClient client

    void setupSpec() {
        kafka.start()
        embeddedServer = ApplicationContext.run(EmbeddedServer,
                ['kafka.bootstrap.servers': kafka.bootstrapServers])
        context = embeddedServer.applicationContext
        client = context.createBean(HttpClient, embeddedServer.URL)
    }

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
