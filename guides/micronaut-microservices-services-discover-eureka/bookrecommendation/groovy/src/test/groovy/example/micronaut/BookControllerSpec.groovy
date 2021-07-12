package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import jakarta.inject.Inject
import spock.lang.IgnoreIf

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @IgnoreIf({env['CI'] as boolean})
    void "retrieve books"() {
        when:
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET("/books"), Argument.listOf(BookRecommendation))

        then:
        response.status() == HttpStatus.OK
        response.body().size() == 1
        response.body().get(0).name == "Building Microservices"
    }
}
