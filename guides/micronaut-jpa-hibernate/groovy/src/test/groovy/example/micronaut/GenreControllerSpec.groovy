package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpHeaders.LOCATION
import static io.micronaut.http.HttpStatus.BAD_REQUEST
import static io.micronaut.http.HttpStatus.CREATED
import static io.micronaut.http.HttpStatus.NOT_FOUND
import static io.micronaut.http.HttpStatus.NO_CONTENT

@MicronautTest // <1>
class GenreControllerSpec extends Specification {

    private BlockingHttpClient blockingClient

    @Inject
    @Client('/')
    HttpClient client // <2>

    void setup() {
        blockingClient = client.toBlocking()
    }

    void supplyAnInvalidOrderTriggersValidationFailure() {

        when:
        blockingClient.exchange(HttpRequest.GET('/genres/list?order=foo'))

        then:
        HttpClientResponseException e = thrown()
        e.response
        BAD_REQUEST == e.status
    }

    void testFindNonExistingGenreReturns404() {
        when:
        blockingClient.exchange(HttpRequest.GET('/genres/99'))

        then:
        HttpClientResponseException e = thrown()
        e.response
        NOT_FOUND == e.status
    }

    void testGenreCrudOperations() {

        given:
        List<Long> genreIds = []

        when:
        HttpRequest<?> request = HttpRequest.POST('/genres', new GenreSaveCommand('DevOps')) // <3>
        HttpResponse<?> response = blockingClient.exchange(request)
        genreIds << entityId(response)

        then:
        CREATED == response.status

        when:
        request = HttpRequest.POST('/genres', new GenreSaveCommand('Microservices')) // <3>
        response = blockingClient.exchange(request)

        then:
        CREATED == response.status

        when:
        Long id = entityId(response)
        genreIds << id
        request = HttpRequest.GET('/genres/' + id)

        Genre genre = blockingClient.retrieve(request, Genre) // <4>

        then:
        'Microservices' == genre.name

        when:
        request = HttpRequest.PUT('/genres', new GenreUpdateCommand(id, 'Micro-services'))
        response = blockingClient.exchange(request)  // <5>

        then:
        NO_CONTENT == response.status

        when:
        request = HttpRequest.GET('/genres/' + id)
        genre = blockingClient.retrieve(request, Genre)


        then:
        'Micro-services' == genre.name

        when:
        request = HttpRequest.GET('/genres/list')
        List<Genre> genres = blockingClient.retrieve(request, Argument.of(List, Genre))

        then:
        2 == genres.size()

        when:
        request = HttpRequest.POST('/genres/ex', new GenreSaveCommand('Microservices')) // <3>
        response = blockingClient.exchange(request)

        then:
        NO_CONTENT == response.status

        when:
        request = HttpRequest.GET('/genres/list')
        genres = blockingClient.retrieve(request, Argument.of(List, Genre))

        then:
        2 == genres.size()

        when:
        request = HttpRequest.GET('/genres/list?max=1')
        genres = blockingClient.retrieve(request, Argument.of(List, Genre))

        then:
        1 == genres.size()
        'DevOps' == genres[0].name

        when:
        request = HttpRequest.GET('/genres/list?max=1&order=desc&sort=name')
        genres = blockingClient.retrieve(request, Argument.of(List, Genre))

        then:
        1 == genres.size()
        'Micro-services' == genres[0].name

        when:
        request = HttpRequest.GET('/genres/list?max=1&offset=10')
        genres = blockingClient.retrieve(request, Argument.of(List, Genre))

        then:
        0 == genres.size()

        cleanup:
        for (Long genreId : genreIds) {
            request = HttpRequest.DELETE('/genres/' + genreId)
            response = blockingClient.exchange(request)
            assert NO_CONTENT == response.status
        }
    }

    private Long entityId(HttpResponse response) {
        String path = '/genres/'
        String value = response.header(LOCATION)
        if (value == null) {
            return null
        }

        int index = value.indexOf(path)
        if (index != -1) {
            return value.substring(index + path.length()) as long
        }

        null
    }
}
