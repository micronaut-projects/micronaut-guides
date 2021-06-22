package example.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
@Property(name = "vat.country", value = "Switzerland") // <2>
@Property(name = "vat.rate", value = "7.7") // <2>
public class VatControllerTest {

    @Inject
    @Client("/")
    public HttpClient httpClient;

    @Test
    void vatExposesTheValueAddedTaxRate() {
        assertEquals(httpClient.toBlocking().retrieve("/vat", BigDecimal.class), new BigDecimal("7.7"));
    }
}
