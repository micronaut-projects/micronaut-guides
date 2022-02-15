package example.micronaut.model;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Model tests for BookAvailability
 */
@Property(name = "spec.name", value = "BookAvailabilityTest") // <6>
@MicronautTest
public class BookAvailabilityTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    /**
     * Model tests for BookAvailability
     */
    @Test
    public void testFromValue() { // <1>
        assertEquals(BookAvailability.AVAILABLE, BookAvailability.fromValue("available"));
        assertEquals(BookAvailability.NOT_AVAILABLE, BookAvailability.fromValue("not available"));
        assertEquals(BookAvailability.RESERVED, BookAvailability.fromValue("reserved"));
    }

    @Test
    public void testToString() { // <2>
        assertEquals("available", BookAvailability.AVAILABLE.toString());
        assertEquals("not available", BookAvailability.NOT_AVAILABLE.toString());
        assertEquals("reserved", BookAvailability.RESERVED.toString());
    }

    @Test
    public void testJsonCreator() { // <7>
        HttpRequest<?> request = HttpRequest.GET(UriBuilder.of("/bookavailability")
                .queryParam("availability", "reserved")
                .build());
        String response = httpClient.toBlocking().retrieve(request);
        assertEquals("reserved", response);
    }

    @Requires(property = "spec.name", value = "BookAvailabilityTest") // <5>
    @Controller("/bookavailability") // <3>
    static class BookAvailabilityController {
        @PermitAll
        @Produces(MediaType.TEXT_PLAIN)
        @Get // <4>
        String index(@QueryValue BookAvailability availability) {
            return availability.toString();
        }
    }
}
