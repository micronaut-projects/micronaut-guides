package example.micronaut;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BookRequestHandlerTest {

    private static BookRequestHandler bookRequestHandler;

    @BeforeAll
    public static void setupServer() {
        bookRequestHandler = new BookRequestHandler(); // <1>
    }

    @AfterAll
    public static void stopServer() {
        if (bookRequestHandler != null) {
            bookRequestHandler.getApplicationContext().close(); // <2>
        }
    }

    @Test
    public void testHandler() {
        Book book = new Book();
        book.setName("Building Microservices");
        BookSaved bookSaved = bookRequestHandler.execute(book);  // <3>
        assertEquals(bookSaved.getName(),book.getName());
        assertNotNull(bookSaved.getIsbn());
    }
}
