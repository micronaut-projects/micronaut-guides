package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.StreamingHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import reactor.core.publisher.Flux
import spock.lang.IgnoreIf
import spock.lang.Specification

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    StreamingHttpClient client

    @IgnoreIf({env['CI'] as boolean})
    void "retrieve books"() {
        when:
        List<BookRecommendation> books = Flux
                .from(client.jsonStream(HttpRequest.GET("/books"), BookRecommendation))
                .collectList()
                .block()

        then:
        books.size() == 1
        books[0].name == "Building Microservices"
    }
}
