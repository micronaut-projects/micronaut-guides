package example.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.micronaut.context.annotation.Property;
import javax.inject.Inject;
import java.math.BigDecimal;

@MicronautTest // <1>
@Property(name = "vat.country", value = "Switzerland")
@Property(name = "vat.rate", value = "7.7")
public class VatControllerTest {

    @Inject
    @Client("/")
    public HttpClient httpClient;

    @Test
    void vatExposesTheValueAddedTaxRate() {
        Assertions.assertEquals(httpClient.toBlocking().retrieve("/vat", BigDecimal.class), new BigDecimal("7.7"));
    }
}
