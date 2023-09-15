package example.micronaut;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface ProductPriceChangesClient {
    @Topic("product-price-changes")
    public void send(@KafkaKey String productCode, ProductPriceChangedEvent event);
}
