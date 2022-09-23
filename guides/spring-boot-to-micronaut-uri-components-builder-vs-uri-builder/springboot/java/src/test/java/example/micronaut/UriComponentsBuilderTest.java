package example.micronaut;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriComponentsBuilderTest {

    @Test
    void youCanUseUriComponentsBuilderToBuildUris() {
        String isbn = "1680502395";
        assertEquals("/book/1680502395?lang=es", UriComponentsBuilder.fromUriString("/book")
                .path("/" + isbn)
                .queryParam("lang", "es")
                .build()
                .toUriString());
    }
}
