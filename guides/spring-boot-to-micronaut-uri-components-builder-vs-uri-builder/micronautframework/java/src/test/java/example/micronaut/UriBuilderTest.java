package example.micronaut;

import io.micronaut.http.uri.UriBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriBuilderTest {

    @Test
    void youCanUseUriBuilderToBuildUris() {
        String isbn = "1680502395";
        assertEquals("/book/1680502395?lang=es", UriBuilder.of("/book")
                .path(isbn)
                .queryParam("lang", "es")
                .build()
                .toString());
    }
}
