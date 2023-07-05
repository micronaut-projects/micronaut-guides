package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@Testcontainers
@MicronautTest
@TestInstance(PER_CLASS)
class BooksControllerTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    IdGenerator idGenerator;

    @Inject
    BookRepository bookRepository;

    @Container
    static GenericContainer dynamoDBLocal =
            new GenericContainer("amazon/dynamodb-local")
                    .withExposedPorts(8000);
    @NonNull
    @Override
    public Map<String, String> getProperties() {
        if (!dynamoDBLocal.isRunning()) {
            dynamoDBLocal.start();
        }
        return CollectionUtils.mapOf(
                "dynamodb-local.host", "localhost",
                        "dynamodb-local.port", dynamoDBLocal.getFirstMappedPort());
    }

    @Test
    void testRetrieveBooks() throws InterruptedException {
        Book releaseIt = new Book(idGenerator.generate(), "1680502395", "Release It!", null);
        bookRepository.save(releaseIt);
        Book continuousDelivery = new Book(idGenerator.generate(), "0321601912", "Continuous Delivery", 4);
        bookRepository.save(continuousDelivery);
        Book buildingMicroservices = new Book(idGenerator.generate(), "1491950358", "Building Microservices", 0);
        bookRepository.save(buildingMicroservices);

        Optional<Book> result = bookRepository.findById(continuousDelivery.getId());
        assertTrue(result.isPresent());
        assertEquals("Continuous Delivery", result.get().name());

        HttpRequest<?> request = HttpRequest.GET("/books");
        BlockingHttpClient client = httpClient.toBlocking();
        List<BookCatalogue> books = client.retrieve(request, Argument.listOf(BookCatalogue.class));
        assertEquals(3, books.size());
        
        assertTrue(books.stream().anyMatch(it -> it.isbn().equals("1491950358") && it.name().equals("Building Microservices")));
        assertTrue(books.stream().anyMatch(it -> it.isbn().equals("0321601912") && it.name().equals("Continuous Delivery")));
        assertTrue(books.stream().anyMatch(it -> it.isbn().equals("1680502395") && it.name().equals("Release It!")));

        bookRepository.delete(releaseIt.getId());
        bookRepository.delete(continuousDelivery.getId());
        bookRepository.delete(buildingMicroservices.getId());
    }
}
