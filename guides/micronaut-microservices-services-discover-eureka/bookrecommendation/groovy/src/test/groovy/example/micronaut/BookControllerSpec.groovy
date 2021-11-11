package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.IgnoreIf
import spock.lang.Specification
import io.micronaut.core.type.Argument

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @IgnoreIf({env['CI'] as boolean})
    void "retrieve books"() {
        when:
        List<BookRecommendation> books = client.toBlocking().retrieve(HttpRequest.GET("/books"), Argument.listOf(BookRecommendation))

        then:
        books.size() == 1
        books[0].name == "Building Microservices"
    }
}
