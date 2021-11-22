package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "vat.percentage", value = "21.0")  // <1>
@MicronautTest(transactional = false)  // <2>
class BookControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Inject
    BookRepository bookRepository;

    @Test
    void recordsUsedForJsonSerialization() {
        String title = "Building Microservices";
        String isbn = "1491950358";
        // <4>
        String about = """
                        Distributed systems have become more fine-grained in the past 10 years, shifting from code-heavy monolithic applications to smaller, self-contained microservices. But developing these systems brings its own set of headaches. With lots of examples and practical advice, this book takes a holistic view of the topics that system architects and administrators must consider when building, managing, and evolving microservice architectures.
                                                
                        Microservice technologies are moving quickly. Author Sam Newman provides you with a firm grounding in the concepts while diving into current solutions for modeling, integrating, testing, deploying, and monitoring your own autonomous services. You’ll follow a fictional company throughout the book to learn how building a microservice architecture affects a single domain.
                                                
                        Discover how microservices allow you to align your system design with your organization’s goals
                        Learn options for integrating a service with the rest of your system
                        Take an incremental approach when splitting monolithic codebases
                        Deploy individual microservices through continuous integration
                        Examine the complexities of testing and monitoring distributed services
                        Manage security with user-to-service and service-to-service models
                        Understand the challenges of scaling microservice architectures
                        """;        
        Book b = new Book(isbn,
            title,
            new BigDecimal("38.15"), 
            about);
        Book book = bookRepository.save(b);
        assertEquals(1, bookRepository.count());
        BlockingHttpClient client = httpClient.toBlocking();
        List<BookForSale> books = client.retrieve(HttpRequest.GET("/books"),
                Argument.listOf(BookForSale.class)); // <5>
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Building Microservices", books.get(0).title());
        assertEquals("1491950358", books.get(0).isbn());
        assertEquals(new BigDecimal("46.16"), books.get(0).price());
        bookRepository.delete(book);
    }
}