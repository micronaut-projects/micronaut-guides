package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest // <1>
class BooksControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    void "it is possible to retrieve books"() {
        when:
        HttpRequest request = HttpRequest.GET("/books") // <3>
        List books = client.toBlocking().retrieve(request, Argument.listOf(Book)) // <4>

        then:
        books.size() == 3
        books.contains(new Book("1491950358", "Building Microservices"))
        books.contains(new Book("1680502395", "Release It!"))
    }
}
