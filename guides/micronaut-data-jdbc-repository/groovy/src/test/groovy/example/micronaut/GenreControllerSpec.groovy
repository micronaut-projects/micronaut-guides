package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.HttpHeaders
import io.micronaut.core.type.Argument
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

import jakarta.inject.Inject

@MicronautTest //<1>
@Stepwise
class GenreControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    def "find non-existing genre should return 404"() {
        when:
        client.toBlocking().exchange(HttpRequest.GET('/genres/99'))

        then:
        HttpClientResponseException exception = thrown()

        exception.response
        exception.status == HttpStatus.NOT_FOUND
    }

    @Unroll
    def "verify post/create operation - adding #entries.value"() {
        given:
        def request = HttpRequest.POST('/genres', [name: entries.value]) // <3>

        when:
        def response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED
        response.header(HttpHeaders.LOCATION) == "/genres/$entries.key"

        where:
        entries << [1:'DevOps', 2:'Microservices']

    }

    def "verify get/read operation"() {
        when:
        def genre = client.toBlocking().retrieve("/genres/2", Genre) //<4>

        then:
        genre.name == 'Microservices'
    }

    def "verify put/update operation"() {
        when:
        def request = HttpRequest.PUT('/genres', new GenreUpdateCommand(2, 'Micro-services'))
        def response = client.toBlocking().exchange(request) //<5>

        then:
        response.status == HttpStatus.NO_CONTENT

        when:
        def genre = client.toBlocking().retrieve('/genres/2', Genre)

        then:
        genre.name == 'Micro-services'
    }

    def "verify list operation"() {
        when:
        def request = HttpRequest.GET('/genres/list')
        def genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size == 2
    }

    def "verify transaction execption does not add genre"() {
        given:
        def request = HttpRequest.POST('/genres/ex', [name: 'Microservices']) //<3>

        when:
        def response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT

        and:
        client.toBlocking().retrieve(HttpRequest.GET('/genres/list'), Argument.listOf(Genre)).size() == 2
    }

    def "verify pageable operation"() {
        when:
        def request = HttpRequest.GET('/genres/list?size=1')
        def genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'DevOps'
    }

    def "verify pagable sort operation"() {
        when:
        def request = HttpRequest.GET('/genres/list?size=1&sort=name,desc')
        def genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'Micro-services'
    }

    def "verify pageable empty page"() {
        when:
        def request = HttpRequest.GET('/genres/list?size=1&page=2')
        def genres = client.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 0

    }

    @Unroll
    def "verify delete operation - id: #id"() {
        given:
        def request = HttpRequest.DELETE("/genres/$id")

        when:
        def response = client.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT

        where:
        id << [1,2]
    }

}
