package example.micronaut;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

@MicronautTest
public class VatControllerTest {

    @Inject
    @Client("/")
    public HttpClient httpClient;

    @Test
    void vatExposesTheValueAddedTaxRate() {
        Assertions.assertEquals(httpClient.toBlocking().retrieve("/vat", BigDecimal.class), new BigDecimal("21.0"));
    }
}
