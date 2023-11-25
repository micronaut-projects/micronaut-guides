package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "datasources.default.schema-generate", value = "CREATE_DROP") // <1>
@Property(name = "datasources.default.url", value = "jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE")
@Property(name = "datasources.default.username", value = "sa")
@Property(name = "datasources.default.password", value = "")
@Property(name = "datasources.default.dialect", value = "H2")
@Property(name = "datasources.default.driver-class-name", value = "org.h2.Driver")
@Property(name = "micronaut.multitenancy.tenantresolver.httpheader.enabled", value = StringUtils.TRUE)
@MicronautTest(transactional = false) // <2>
class BookControllerTest {

    @Test
    void multitenancyRequest(@Client("/") HttpClient httpClient, // <3>
                             BookRepository bookRepository) {
        bookRepository.save(new Book(null, "Building Microservices with Micronaut", "micronaut"));
        bookRepository.save(new Book(null, "Introducing Micronaut", "micronaut"));

        bookRepository.save(new Book(null, "Grails 3 - Step by Step", "grails"));
        bookRepository.save(new Book(null, "Falando de Grail", "grails"));
        bookRepository.save(new Book(null, "Grails Goodness Notebook", "grails"));

        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> mnRequest = HttpRequest.GET("/books").header("tenantId", "micronaut");
        Argument<List<Book>> responseArgument = Argument.listOf(Book.class);
        HttpResponse<List<Book>> response = assertDoesNotThrow(() -> client.exchange(mnRequest, responseArgument));
        assertEquals(HttpStatus.OK, response.getStatus());
        List<Book> books = response.body();
        assertNotNull(books);
        assertEquals(2, books.size());

        HttpRequest<?> grailsRequest = HttpRequest.GET("/books").header("tenantId", "grails");
        response = assertDoesNotThrow(() -> client.exchange(grailsRequest, responseArgument));
        assertEquals(HttpStatus.OK, response.getStatus());
        books = response.body();
        assertNotNull(books);
        assertEquals(3, books.size());

        bookRepository.deleteAll();
    }

}
