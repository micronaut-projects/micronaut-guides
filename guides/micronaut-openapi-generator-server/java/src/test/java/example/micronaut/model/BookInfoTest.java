package example.micronaut.model;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Model tests for BookInfo
 */
@Property(name = "spec.name", value = "BookInfoTest")
@MicronautTest
public class BookInfoTest {
    @Inject
    Validator validator;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void bookInfoJsonSerialization() {
        String json = httpClient.toBlocking().retrieve(HttpRequest.GET("/bookinfo"));
        assertEquals("{\"name\":\"Alice's Adventures in Wonderland\",\"availability\":\"available\",\"author\":\"Lewis Carroll\",\"ISBN\":\"9783161484100\"}", json);
    }

    @Requires(property = "spec.name", value = "BookInfoTest")
    @Controller("/bookinfo")
    static class BookInfoSerdeController {
        @PermitAll
        @Get
        BookInfo index() {
            return new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                    .author("Lewis Carroll")
                    .ISBN("9783161484100");
        }
    }

    @Test
    public void validBookInfo() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE);
        assertTrue(validator.validate(bookInfo).isEmpty());
    }

    /**
     * Model tests for BookInfo
     */
    @Test
    public void testBookInfo() {
        BookInfo bookInfo = new BookInfo(null, BookAvailability.AVAILABLE);
        assertFalse(validator.validate(bookInfo).isEmpty());
    }

    /**
     * Test the property 'name'
     */
    @Test
    public void nameTest() {
        BookInfo bookInfo = new BookInfo(null, BookAvailability.AVAILABLE);
        assertFalse(validator.validate(bookInfo).isEmpty());
    }

    /**
     * Test the property 'availability'
     */
    @Test
    public void availabilityTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", null);
        assertFalse(validator.validate(bookInfo).isEmpty());
    }

    /**
     * Test the property 'author'
     */
    @Test
    public void authorTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author("fo");
        assertFalse(validator.validate(bookInfo).isEmpty());
        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author("Lewis Carroll");
        assertTrue(validator.validate(bookInfo).isEmpty());
        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author(null);
        assertTrue(validator.validate(bookInfo).isEmpty());
    }

    /**
     * Test the property 'ISBN'
     */
    @Test
    public void ISBNTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN(null);
        assertTrue(validator.validate(bookInfo).isEmpty());

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN("9783161484100");
        assertTrue(validator.validate(bookInfo).isEmpty());

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN("978316148410");
        assertFalse(validator.validate(bookInfo).isEmpty());
    }
}
