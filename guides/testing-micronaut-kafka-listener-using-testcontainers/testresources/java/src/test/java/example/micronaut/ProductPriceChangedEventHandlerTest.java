package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@MicronautTest(transactional = false) // <1>
class ProductPriceChangedEventHandlerTest {

    @Test
    void shouldHandleProductPriceChangedEvent(
            ProductPriceChangesClient productPriceChangesClient,
            ProductRepository productRepository
    ) {
        Product product = new Product(null, "P100", "Product One", BigDecimal.TEN);
        Long id = productRepository.save(product).getId(); // <2>

        ProductPriceChangedEvent event = new ProductPriceChangedEvent(
                "P100",
                new BigDecimal("14.50")
        );

        productPriceChangesClient.send(event.productCode(), event); // <3>

        await() // <4>
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    Optional<Product> optionalProduct = productRepository.findByCode("P100");
                    assertThat(optionalProduct).isPresent();
                    assertThat(optionalProduct.get().getCode()).isEqualTo("P100");
                    assertThat(optionalProduct.get().getPrice()).isEqualTo(new BigDecimal("14.50"));
                });

        productRepository.deleteById(id);
    }
}
