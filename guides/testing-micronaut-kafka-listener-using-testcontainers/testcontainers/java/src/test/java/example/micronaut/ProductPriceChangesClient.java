package example.micronaut;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient // <1>
public interface ProductPriceChangesClient {

    @Topic("product-price-changes") // <2>
    void send(@KafkaKey String productCode, // <3>
              ProductPriceChangedEvent event);
}
