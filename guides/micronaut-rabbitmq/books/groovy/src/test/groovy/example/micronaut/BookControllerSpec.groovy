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

import jakarta.inject.Inject

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    AnalyticsClient analyticsClient

    @Inject
    @Client('/')
    HttpClient client

    void 'test message is published to RabbitMQ when book found'() {
        when:
        Optional<Book> result = retrieveGet('/books/1491950358')

        then:
        result
        result.present
        1 * analyticsClient.updateAnalytics(_)
    }

    void 'test message is not published to RabbitMQ when book not found'() {
        when:
        retrieveGet '/books/INVALID'

        then:
        thrown HttpClientResponseException
        0 * analyticsClient.updateAnalytics(_)
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return Mock(AnalyticsClient)
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional, Book))
    }
}
