package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class BookControllerSpec extends Specification {

    private int clientInvocationCount

    @Inject
    AnalyticsClient analyticsClient

    @Inject
    @Client('/')
    HttpClient client

    void 'test message is published to Kafka when book found'() {
        when:
        Optional<Book> result = retrieveGet('/books/1491950358')

        then:
        result != null
        result.present
        1 == clientInvocationCount
    }

    void 'test message is not published to Kafka when book not found'() {
        when:
        retrieveGet '/books/INVALID'

        then:
        thrown HttpClientResponseException
        0 == clientInvocationCount
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return new AnalyticsClient() {
            void updateAnalytics(Book book) {
                clientInvocationCount++
            }
        }
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional, Book))
    }
}
