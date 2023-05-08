package example.micronaut.model;

//tag::imports[]
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
//end::imports[]

/**
 * Model tests for BookInfo
 */
//tag::annotations[]
@Property(name = "spec.name", value = "BookInfoTest") // <2>
@MicronautTest
public class BookInfoTest {
//end::annotations[]
    //tag::requiredProperties[]
    @Inject
    Validator validator; // <1>

    @Test
    public void nameTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE);
        assertTrue(validator.validate(bookInfo).isEmpty()); // <2>

        bookInfo = new BookInfo(null, BookAvailability.AVAILABLE);
        assertFalse(validator.validate(bookInfo).isEmpty()); // <3>
    }

    @Test
    public void availabilityTest() { // <4>
        BookInfo bookInfo = new BookInfo("ALice's Adventures in Wonderland", BookAvailability.RESERVED);
        assertTrue(validator.validate(bookInfo).isEmpty());

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", null);
        assertFalse(validator.validate(bookInfo).isEmpty());
    }
    //end::requiredProperties[]

    //tag::otherProperties[]
    @Test
    public void authorTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author(null);
        assertTrue(validator.validate(bookInfo).isEmpty());

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author("Lewis Carroll");
        assertTrue(validator.validate(bookInfo).isEmpty()); // <1>

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author("fo");
        assertFalse(validator.validate(bookInfo).isEmpty()); // <2>
    }

    @Test
    public void ISBNTest() {
        BookInfo bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN(null);
        assertTrue(validator.validate(bookInfo).isEmpty());

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN("9783161484100");
        assertTrue(validator.validate(bookInfo).isEmpty()); // <3>

        bookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .ISBN("9783161 84100");
        assertFalse(validator.validate(bookInfo).isEmpty()); // <4>
    }
    //end::otherProperties[]

    //tag::jsonSerialization[]
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    public void bookInfoJsonSerialization() {
        BookInfo requiredBookInfo = new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                .author("Lewis Carroll")
                .ISBN("9783161484100");

        BookInfo bookInfo = httpClient.toBlocking().retrieve(HttpRequest.GET("/bookinfo"), BookInfo.class); // <5>
        assertEquals(requiredBookInfo, bookInfo);
    }

    @Requires(property = "spec.name", value = "BookInfoTest") // <3>
    @Controller("/bookinfo") // <1>
    static class BookInfoSerdeController {
        @PermitAll
        @Get
        BookInfo index() { // <4>
            return new BookInfo("Alice's Adventures in Wonderland", BookAvailability.AVAILABLE)
                    .author("Lewis Carroll")
                    .ISBN("9783161484100");
        }
    }
    //end::jsonSerialization[]
}
