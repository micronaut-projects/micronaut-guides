package example.micronaut;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface AnalyticsClient {

    @Topic("analytics") // <1>
    void updateAnalytics(Book book); // <2>
}
