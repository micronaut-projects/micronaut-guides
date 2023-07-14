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

    @Test
    void vatExposesTheValueAddedTaxRate(@Client("/") HttpClient httpClient) {
        assertEquals("{\"rate\":7.7}", httpClient.toBlocking().retrieve("/vat"));
    }
}
