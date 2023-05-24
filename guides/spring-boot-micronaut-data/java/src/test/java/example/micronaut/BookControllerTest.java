package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class BookControllerTest {
    @LocalServerPort // <2>
    private int port;

    @Autowired // <3>
    private TestRestTemplate restTemplate;

    @Autowired // <4>
    BookRepository bookRepository;

    @Test
    void booksGet() {
        assertEquals(0, booksJsonArray().length);
        Book moreJava17 = bookRepository.save("More Java 17", 951);
        Book[] books = booksJsonArray();
        assertEquals(1, books.length);
        assertNotNull(books[0].id());
        assertEquals(books[0].pages(), 951);
        assertEquals(books[0].title(), "More Java 17");
        bookRepository.delete(moreJava17);
        assertEquals(0, booksJsonArray().length);
    }

    private Book[] booksJsonArray() {
        return restTemplate.getForObject(booksRequestUriString(), Book[].class);
    }

    private String booksRequestUriString() {
        return UriComponentsBuilder.fromUriString("http://localhost:" + port)
                .path("books")
                .build()
                .toUriString();
    }

}
