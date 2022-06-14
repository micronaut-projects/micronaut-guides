package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Unroll

@MicronautTest //<1>
class GenreControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    void "find non-existing genre should return 404"() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/genres/99'))

        then:
        HttpClientResponseException exception = thrown()

        exception.response
        exception.status == HttpStatus.NOT_FOUND
    }

    @Unroll
    void "genre CRUD operations"() {
        given: "verify post/create operation - adding #entries.value"
        HttpRequest<?> request = HttpRequest.POST('/genres', [name: 'DevOps']) // <3>

        when:
        HttpResponse<?> response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED

        when:
        String location = response.header(HttpHeaders.LOCATION)

        then:
        location.startsWith("/genres/")

        when:
        Long devOpsId = Long.valueOf(location.substring("/genres/".length()))

        then:
        noExceptionThrown()

        when:
        request = HttpRequest.POST('/genres', [name: 'Microservices']) // <3>
        response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED

        when:
        location = response.header(HttpHeaders.LOCATION)

        then:
        location.startsWith("/genres/")

        when:
        Long microservicesId = Long.valueOf(location.substring("/genres/".length()))

        then:
        noExceptionThrown()

        when: "verify get/read operation"
        URI microservicesUri = UriBuilder.of("/genres").path(""+microservicesId).build()
        Genre genre = client.toBlocking().retrieve(microservicesUri.toString(), Genre) //<4>

        then:
        genre.name == 'Microservices'

        when: "verify put/update operation"
        request = HttpRequest.PUT('/genres', new GenreUpdateCommand(microservicesId, 'Micro-services'))
        response = client.toBlocking().exchange(request) //<5>

        then:
        response.status == HttpStatus.NO_CONTENT

        when:
        genre = client.toBlocking().retrieve(microservicesUri.toString(), Genre)

        then:
        genre.name == 'Micro-services'

        when: "verify list operation"
        request = HttpRequest.GET('/genres/list')
        List<Genre> genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 2

        when: "verify transaction execption does not add genre"
        request = HttpRequest.POST('/genres/ex', [name: 'Microservices']) //<3>
        response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT

        and:
        client.toBlocking().retrieve(HttpRequest.GET('/genres/list'), Argument.listOf(Genre)).size() == 2

        when: "verify pageable operation"
        request = HttpRequest.GET(UriBuilder.of("/genres")
                .path("list")
                .queryParam("size", 1)
                .build())
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'DevOps'

        when: "verify pagable sort operation"
        request =  HttpRequest.GET(UriBuilder.of("/genres")
                .path("list")
                .queryParam("size", 1)
                .queryParam("sort", "name,desc")
                .build())
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'Micro-services'

        when: "verify pageable empty page"
        request =  HttpRequest.GET(UriBuilder.of("/genres")
                .path("list")
                .queryParam("size", 1)
                .queryParam("page", 2)
                .build())
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 0

        when: "verify delete operation - id: #id"
        request = HttpRequest.DELETE(UriBuilder.of("/genres").path("" + microservicesId).build().toString())
        response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT

        when:
        request = HttpRequest.DELETE(UriBuilder.of("/genres").path("" + devOpsId).build().toString())
        response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT
    }
}
