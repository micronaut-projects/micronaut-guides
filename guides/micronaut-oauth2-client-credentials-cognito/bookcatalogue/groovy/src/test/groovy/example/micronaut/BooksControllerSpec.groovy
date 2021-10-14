package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import io.micronaut.core.util.StringUtils
import io.micronaut.context.annotation.Property

@Property(name = 'micronaut.security.enabled', value= StringUtils.FALSE)
@MicronautTest
class BooksControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "it is possible to retrieve books"() {
        when:
        HttpRequest request = HttpRequest.GET("/books")
        List<Book> books = client.toBlocking().retrieve(request, Argument.listOf(Book))

        then:
        books.size() == 3
        books.contains(new Book("1491950358", "Building Microservices"))
        books.contains(new Book("1680502395", "Release It!"))
    }
}
