package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.NOT_FOUND
import static io.micronaut.http.HttpStatus.OK

@MicronautTest
class BooksControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void "for a book with inventory true is returned"() {
        when:
        HttpResponse<Boolean> rsp = httpClient.toBlocking().exchange(
                HttpRequest.GET("/books/stock/1491950358"), Boolean)

        then:
        rsp.status() == OK
        rsp.body()
    }

    void "for an invalid ISBN 404 is returned"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET("/books/stock/XXXXX"), Boolean)

        then:
        HttpClientResponseException e = thrown()
        e.response.status == NOT_FOUND
    }
}
