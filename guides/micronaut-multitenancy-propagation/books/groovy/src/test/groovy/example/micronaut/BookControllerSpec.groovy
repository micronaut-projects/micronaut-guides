package example.micronaut

import grails.gorm.multitenancy.Tenants
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import javax.inject.Inject

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @Inject
    BookService bookService

    void "test hello world response"() {
        given:
        List<Long> ids = []
        Tenants.withId("sherlock") {
            ids << bookService.save('Sherlock diary').id
        }
        Tenants.withId("watson") {
            ids << bookService.save('Watson diary').id
        }

        when:
        HttpRequest<?> request = booksRequest("sherlock")
        List<BookResponse> rsp  = client.toBlocking().retrieve(request, Argument.listOf(BookResponse))

        then:
        rsp
        rsp.size() == 1
        rsp.first().title == 'Sherlock diary'

        when:
        request = booksRequest("watson")
        rsp  = client.toBlocking().retrieve(request, Argument.listOf(BookResponse))

        then:
        rsp.size() == 1
        rsp.first().title == 'Watson diary'

        cleanup:
        ids.each {
            bookService.delete(it)
        }
    }

    private static HttpRequest<?> booksRequest(String tenantId) {
        HttpRequest.GET('/books')
                .header("tenantId", tenantId)
    }
}
