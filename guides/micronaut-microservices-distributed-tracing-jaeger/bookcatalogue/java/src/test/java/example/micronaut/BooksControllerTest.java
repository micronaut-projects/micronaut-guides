package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class BooksControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testRetrieveBooks() {
        HttpRequest request = HttpRequest.GET("/books");
        List books = client.toBlocking().retrieve(request, Argument.listOf(Book.class));
        assertEquals(3, books.size());
        assertTrue(books.contains(new Book("1491950358", "Building Microservices")));
        assertTrue(books.contains(new Book("1680502395", "Release It!")));
    }
}
