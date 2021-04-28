package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

    void "test hello world response"() {
        when:
        HttpRequest request = booksRequest("sherlock")
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
    }

    private static HttpRequest booksRequest(String tenantId) {
        HttpRequest.GET('/books')
                .header("tenantId", tenantId)
    }
}
