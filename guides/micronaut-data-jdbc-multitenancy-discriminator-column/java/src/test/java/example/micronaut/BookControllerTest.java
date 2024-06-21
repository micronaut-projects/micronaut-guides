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
import java.util.Map;

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
        BlockingHttpClient client = httpClient.toBlocking();
        save(bookRepository, client,  "Building Microservices with Micronaut", "micronaut");
        save(bookRepository, client, "Introducing Micronaut", "micronaut");
        save(bookRepository, client, "Grails 3 - Step by Step", "grails");
        save(bookRepository, client, "Falando de Grail", "grails");
        save(bookRepository, client, "Grails Goodness Notebook", "grails");

        List<Book> books = fetchBooks(client, "micronaut");
        assertNotNull(books);
        assertEquals(2, books.size());

        books = fetchBooks(client, "grails");
        assertNotNull(books);
        assertEquals(3, books.size());
        bookRepository.deleteAll();
    }

    List<Book> fetchBooks(BlockingHttpClient client, String framework) {
        HttpRequest<?> request = HttpRequest.GET("/books").header("tenantId", framework);
        Argument<List<Book>> responseArgument = Argument.listOf(Book.class);
        HttpResponse<List<Book>> response = assertDoesNotThrow(() -> client.exchange(request, responseArgument));
        assertEquals(HttpStatus.OK, response.getStatus());
        return response.body();
    }

    void save(BookRepository bookRepository, BlockingHttpClient client, String title, String framework) {
        bookRepository.save(new Book(null, title, framework));
    }
}
