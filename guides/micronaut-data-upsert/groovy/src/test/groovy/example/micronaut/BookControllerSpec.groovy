package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest // <1>
class BookControllerSpec extends AbstractSpec { // <2>

    @Inject
    BookRepository repository // <3>

    @Inject
    @Client('/')
    HttpClient httpClient // <4>

    void 'POST and PUT upsert a book'() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        String path = '/books'
        String title = 'Netty in Action'

        expect:
        !repository.findByTitle(title) // <5>

        when:
        HttpResponse<?> postResponse = client.exchange(HttpRequest.POST(path, [id: 123L, title: title]))

        then:
        postResponse.status == HttpStatus.CREATED // <6>
        repository.findByTitle(title)
        repository.findByTitle(title).pages == null

        when:
        HttpResponse<?> putResponse = client.exchange(HttpRequest.PUT(path, [id: 123L, title: title, pages: 265]))

        then:
        putResponse.status == HttpStatus.OK // <7>
        repository.findByTitle(title).pages == 265 // <8>
    }
}
