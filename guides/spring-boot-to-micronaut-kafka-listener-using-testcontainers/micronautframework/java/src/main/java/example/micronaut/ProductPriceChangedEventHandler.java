package example.micronaut;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;

@Singleton
@Transactional
class ProductPriceChangedEventHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ProductPriceChangedEventHandler.class);

  private final ProductRepository productRepository;

  ProductPriceChangedEventHandler(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Topic("product-price-changes")
  @KafkaListener(offsetReset = EARLIEST, groupId = "demo")
  public void handle(ProductPriceChangedEvent event) {
    LOG.info("Received a ProductPriceChangedEvent with productCode:{}: ", event.productCode());
    productRepository.updateProductPrice(event.productCode(), event.price());
  }
}
