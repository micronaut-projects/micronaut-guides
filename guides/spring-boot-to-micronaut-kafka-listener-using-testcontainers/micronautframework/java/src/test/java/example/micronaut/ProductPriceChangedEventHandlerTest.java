package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@MicronautTest(transactional = false)
@Property(name = "datasources.default.url", value = "jdbc:tc:mysql:8.0.32:///db")
@Property(name = "datasources.default.driver-class-name", value = "org.testcontainers.jdbc.ContainerDatabaseDriver")
@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductPriceChangedEventHandlerTest implements TestPropertyProvider {
  @Container
  static final KafkaContainer kafka = new KafkaContainer(
    DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
  );

  @Override
  public @NonNull Map<String, String> getProperties() {
    if (!kafka.isRunning()) {
      kafka.start();
    }
    return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
  }

  @Inject
  ProductPriceChangesClient productPriceChangesClient;

  @Inject
  ProductRepository productRepository;


  @Test
  void shouldHandleProductPriceChangedEvent() {
    Product product = new Product(null, "P100", "Product One", BigDecimal.TEN);
    Long id = productRepository.save(product).getId();

    ProductPriceChangedEvent event = new ProductPriceChangedEvent(
      "P100",
      new BigDecimal("14.50")
    );

    productPriceChangesClient.send(event.productCode(), event);

    await()
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
